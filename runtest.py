#!/usr/bin/env python3

import os
import sys
import subprocess
import tempfile

from typing import List

import nbformat

def _notebook_run(path):
    """Execute a notebook via nbconvert and collect output.
       :returns (parsed nb object, execution errors)
    """
    dirname, __ = os.path.split(path)
    if len(dirname) > 0:
        os.chdir(dirname)
    with tempfile.NamedTemporaryFile(suffix=".ipynb", mode = 'w+') as fout:
        args = ["jupyter-nbconvert", "--to", "notebook", "--execute",
          "--allow-errors",
          "--ExecutePreprocessor.timeout=60",
          "--output", fout.name, path]
        subprocess.check_call(args)

        fout.seek(0)
        nb = nbformat.read(fout, nbformat.current_nbformat)

    errors = [output for cell in nb.cells if "outputs" in cell
                     for output in cell["outputs"]\
                     if output.output_type == "error"]

    return nb, errors

def check_errors(expected, actual):
    assert len(expected) == len(actual)
    for e, a in zip(expected, actual):
        # print(a['traceback'])
        # print('\n\n')
        assert e in a['traceback'][0]

notebooks = {
    "1_intro_to_scala.ipynb": [],
    "2.1_first_module.ipynb": ['scala.NotImplementedError'],
    "2.2_comb_logic.ipynb": ['Compilation Failed'] + ['scala.NotImplementedError'] * 3,
    "2.3_control_flow.ipynb": ['scala.NotImplementedError'] * 2 + ['Compilation Failed']
      + ['scala.NotImplementedError'] + ['Compilation Failed'],
    "2.4_sequential_logic.ipynb": ['scala.NotImplementedError', 'scala.NotImplementedError'],
    "2.5_exercise.ipynb": ['scala.NotImplementedError'] * 3 +
      ['java.nio.file.NoSuchFileException'] + ['Compilation Failed'] * 2,
    "3.1_parameters.ipynb": ['java.util.NoSuchElementException'],
    "3.2_collections.ipynb": ['Internal Error!'],
    "3.2_interlude.ipynb": [],
    "3.3_higher-order_functions.ipynb": ['scala.NotImplementedError'] +
      ['java.lang.UnsupportedOperationException'] + ['scala.NotImplementedError'] * 3,
    "3.4_functional_programming.ipynb": ['scala.NotImplementedError'] +
      ['Compilation Failed'] + ['scala.NotImplementedError'] + ['Compilation Failed'],
    "3.5_object_oriented_programming.ipynb": ['Compilation Failed'],
    "3.6_types.ipynb": ['chisel3.internal.ChiselException'] + ['I give up!'] + ['SyntaxError'] + ['Compilation Failed'] * 6,
    "4.1_firrtl_ast.ipynb": [],
    "4.2_firrtl_ast_traversal.ipynb": [],
    "4.3_firrtl_common_idioms.ipynb": [],
    "4.4_firrtl_add_ops_per_module.ipynb": [],
}

if __name__ == "__main__":
    notebooks_to_run = []  # type: List[str]
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
        check_errors(expected, errors)
