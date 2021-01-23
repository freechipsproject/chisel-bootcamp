#!/usr/bin/env python3
import itertools
import os
import sys
import subprocess
import tempfile

from typing import Any, Dict, List

import nbformat


def _notebook_run(path):
    """Execute a notebook via nbconvert and collect output.
       :returns (parsed nb object, execution errors)
    """
    dirname, __ = os.path.split(path)
    if len(dirname) > 0:
        os.chdir(dirname)
    with tempfile.NamedTemporaryFile(suffix=".ipynb", mode='w+') as fout:
        args = ["jupyter-nbconvert", "--to", "notebook", "--execute",
                "--allow-errors",
                "--ExecutePreprocessor.timeout=60",
                "--output", fout.name, path]
        subprocess.check_call(args, stderr=True)

        fout.seek(0)
        nb = nbformat.read(fout, nbformat.current_nbformat)

    errors = [output for cell in nb.cells if "outputs" in cell
              for output in cell["outputs"] \
              if output.output_type == "error"]

    return nb, errors


def check_errors(file_name, expected: List[str], actual: List[Any]) -> bool:
    """When errors occur they are due to a mismatch in the errors that occurred at runtime
    and the expected error that are defined in notebooks
    This produces huge output but the relevant information is between bars of '=' at the end.
    Look at what was expected and either fix the error or add the text of the error that occurred
    :note Errors shown may have hidden escape sequence for colors etc. When pasting be careful about this.

    :param file_name: name of file where errors occurred
    :param expected:  errors that were expected
    :param actual:    errors that occurred.
    :return:
    """
    actual_tracebacks: List[str] = list(map(lambda x: str(x['traceback'][0][:100]), actual))

    return_value = True
    for i, (e, a) in enumerate(itertools.zip_longest(expected, actual_tracebacks, fillvalue="-- No Error --")):
        if e not in a:
            if return_value:
                print("=" * 100, file=sys.stderr)
                print(f"Errors detected in {file_name}", file=sys.stderr)
            print(f"No match for {i}-th expected error '{e}' got '{a[:100]}'", file=sys.stderr)
            return_value = False
    if not return_value:
        print("=" * 100, file=sys.stderr)
    return return_value


notebooks: Dict[str, List[str]] = {
    # This is the list of pages to try and rum along with each pages expected errors.

    "1_intro_to_scala.ipynb": [],
    "2.1_first_module.ipynb": ["chisel3.internal.ChiselException: Exception thrown when elaborating ChiselGeneratorAnnotation"],
    "2.2_comb_logic.ipynb": ['Compilation Failed'] +
                            ['chisel3.internal.ChiselException: Exception thrown when elaborating ChiselGeneratorAnnotation'] +
                            ['chisel3.internal.ChiselException: Exception thrown when elaborating ChiselGeneratorAnnotation'] +
                            ['chisel3.internal.ChiselException: Exception thrown when elaborating ChiselGeneratorAnnotation'],
    "2.3_control_flow.ipynb": ['scala.NotImplementedError'] * 2 +
                              ['Compilation Failed'] +
                              ['scala.NotImplementedError'] +
                              ['Compilation Failed'],
    "2.4_sequential_logic.ipynb": ['chisel3.internal.ChiselException: Exception thrown when elaborating ChiselGeneratorAnnotation'] * 2,
    "2.5_exercise.ipynb": ['chisel3.internal.ChiselException: Exception thrown when elaborating ChiselGeneratorAnnotation'] * 3 +
                          ['Compilation Failed'],
    "2.6_chiseltest.ipynb": [],
    "3.1_parameters.ipynb": ['java.util.NoSuchElementException'],
    "3.2_collections.ipynb": [
        'chisel3.internal.ChiselException: Exception thrown when elaborating ChiselGeneratorAnnotation'],
    "3.2_interlude.ipynb": [],
    "3.3_higher-order_functions.ipynb": ['scala.NotImplementedError'] +
                                        ['java.lang.UnsupportedOperationException'] +
                                        ['scala.NotImplementedError'] * 2 +
                                        ['chisel3.internal.ChiselException: Exception thrown when elaborating ChiselGeneratorAnnotation'],
    "3.4_functional_programming.ipynb": ['scala.NotImplementedError'] +
                                        ['Compilation Failed'] +
                                        ['scala.NotImplementedError'] +
                                        ['Compilation Failed'],
    "3.5_object_oriented_programming.ipynb": ['Compilation Failed'],
    "3.6_types.ipynb": ['chisel3.internal.ChiselException: Connection between sink'] +
                       ['Failed to elaborate Chisel circuit'] +
                       ['expected ")"'] +
                       ['scala.MatchError: ChiselExecutionFailure'] +
                       ['Compilation Failed'] * 5,
    "4.1_firrtl_ast.ipynb": [],
    "4.2_firrtl_ast_traversal.ipynb": [],
    "4.3_firrtl_common_idioms.ipynb": [],
    "4.4_firrtl_add_ops_per_module.ipynb": ['FirrtlInternalException'],  # bug 129
}

if __name__ == "__main__":
    notebooks_to_run: List[str] = []
    if len(sys.argv) > 1:
        if sys.argv[1] == "--help":
            print("Usage: {} [notebook_name.ipynb] [notebook_name_2.ipynb] [...]".format(sys.argv[0]))
            print("By default, check all notebooks if notebooks are not specified.")
            sys.exit(0)
        else:
            notebooks_to_run = sys.argv[1:]
    else:
        notebooks_to_run = sorted(notebooks)  # all notebooks
    for n in notebooks_to_run:
        expected = notebooks[n]
        nb, errors = _notebook_run(n)
        assert check_errors(n, expected, errors)
