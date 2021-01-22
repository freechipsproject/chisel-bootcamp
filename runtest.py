#!/usr/bin/env python3

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

def check_errors(expected: List[str], actual: List[Any]) -> bool:
    if len(expected) != len(actual):
        actual_tracebacks: List[str] = list(map(lambda x: str(x['traceback'][0]), actual))
        print(f"Expected errors: {expected}", file=sys.stderr)
        print(f"Actual errors: {actual_tracebacks}", file=sys.stderr)
        return False
    for e, a in zip(expected, actual):
        if e not in a['traceback'][0]:
            print(f"Expected error = {e}, actual error = {a['traceback'][0]}", file=sys.stderr)
            return False
    return True

notebooks: Dict[str, List[str]] = {
    # For some reason the NotImplementedError gets swallowed up by
    # setupFirrtlTerpBackend.
    # Instead we have to try and catch the "java.lang.Exception: Problem with compilation"
    # message instead.

    "1_intro_to_scala.ipynb": [],
    "2.1_first_module.ipynb": ['scala.NotImplementedError'],
    "2.2_comb_logic.ipynb": ['Compilation Failed'] + ['java.lang.Exception: Problem with compilation'] * 3,
    "2.3_control_flow.ipynb": ['scala.NotImplementedError'] * 2 + ['Compilation Failed']
      + ['scala.NotImplementedError'] + ['Compilation Failed'],
    "2.4_sequential_logic.ipynb": ['java.lang.Exception: Problem with compilation'] * 2,
    "2.5_exercise.ipynb": ['java.lang.Exception: Problem with compilation'] * 4,
    "2.6_testers2.ipynb": [],
    "3.1_parameters.ipynb": ['java.util.NoSuchElementException'],
    "3.2_collections.ipynb": ['java.lang.Exception: Problem with compilation'],
    "3.2_interlude.ipynb": [],
    "3.3_higher-order_functions.ipynb": ['scala.NotImplementedError'] +
      ['java.lang.UnsupportedOperationException'] + ['scala.NotImplementedError'] * 2 + ['java.lang.Exception: Problem with compilation'],
    "3.4_functional_programming.ipynb": ['scala.NotImplementedError'] +
      ['Compilation Failed']
      + ['scala.NotImplementedError'] + ['Compilation Failed'],
    "3.5_object_oriented_programming.ipynb": ['Compilation Failed'],
    # The first ChiselException swallowed by setupFirrtlTerpBackend
    "3.6_types.ipynb": ['java.lang.Exception: Problem with compilation'] + ['Failed to elaborate Chisel circuit'] + ['expected ")"'] + ['Compilation Failed'] * 5,
    "4.1_firrtl_ast.ipynb": [],
    "4.2_firrtl_ast_traversal.ipynb": [],
    "4.3_firrtl_common_idioms.ipynb": [],
    "4.4_firrtl_add_ops_per_module.ipynb": ['FirrtlInternalException'], # bug 129
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
        assert check_errors(expected, errors)
