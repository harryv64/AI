from random import choice

# CLASSES #
class QueenBoard:
    """
    ------------------------------------------------------
    A QueenBoard object that keeps track of the conflicting
    queens and can update their constraints.
    ------------------------------------------------------
    """
    def __init__(self, n):
        self._n = n
        self._rowsqueen = {i: set() for i in range(1, self._n+1)}
        self._positivediaQ = {i: set() for i in range(1, 2 * self._n)}
        self._negativediagQ = {i: set() for i in range(1, 2 * self._n)}

    def set_queen(self, x, y, constraints):
        """
        ------------------------------------------------------
        Set a queen on the QueenBoard      ------------------------------------------------------
        Inputs:
            x - the x value on the board
            y - the y value on the board
            constraints - the dict of conflicts for each
                queen
        ------------------------------------------------------
        """
        # get the num_conflict set of conflicted queens
        num_conflict = self._rowsqueen[y] | self._positivediaQ[y+(x-1)] | self._negativediagQ[y + (self._n - x)]

        # update the number of conflicts for each queen by 1
        for i in num_conflict:
            constraints[i] += 1

        # add the queen to the board
        self._rowsqueen[y].add(x)
        self._positivediaQ[y+(x-1)].add(x)
        self._negativediagQ[y+(self._n - x)].add(x)

        # update number of conflicts
        constraints[x] = len(num_conflict)
        return

    def delete_queen(self, x, y, constraints):
        """
        ------------------------------------------------------
        Removes a queen on the board.
        ------------------------------------------------------
        Inputs:
            x - the x value on the board
            y - the y value on the board
            constraints - the dict of conflicts for each
                queen
        ------------------------------------------------------
        """
        # get the combined set of conflicted queens
        num_conflict = self._rowsqueen[y] | self._positivediaQ[y+(x-1)] | self._negativediagQ[y + (self._n - x)]

        # update the number of conflicts for each queen by 1
        for i in num_conflict:
            constraints[i] -= 1

        # removes the queen from the board
        self._rowsqueen[y].remove(x)
        self._positivediaQ[y+(x-1)].remove(x)
        self._negativediagQ[y+(self._n - x)].remove(x)

        # update the number of conflicts
        constraints[x] = 0
        return

    def get_num_conflicts(self, x, y):
        """
        ------------------------------------------------------
        Get the number of conflicts for a point on the
        board.
        ------------------------------------------------------
        Inputs:
            x - the x value on the board
            y - the y value on the board
        Returns:
            c - the number of conflicted queens
        ------------------------------------------------------
        """
        # get the combined set of conflicted queens
        num_conflict = self._rowsqueen[y] | self._positivediaQ[y+(x-1)] | self._negativediagQ[y + (self._n - x)]

        return len(num_conflict)


class CSP:
    """
    ------------------------------------------------------
    A CSP object that holds the variables, domains and
    constraints.
    ------------------------------------------------------
    """
    def __init__(self, variables , domains, constraints):
        self.variables = variables
        self.domains = domains
        self.constraints = constraints


class Print_colors:
    """
    ------------------------------------------------------
    Colours for the terminal printing.
    ------------------------------------------------------
    """
    HEADER = '\033[95m'
    OKBLUE = '\033[94m'
    OKGREEN = '\033[92m'
    WARNING = '\033[93m'
    FAIL = '\033[91m'
    ENDC = '\033[0m'
    BOLD = '\033[1m'
    UNDERLINE = '\033[4m'


# FUNCTIONS #
def num_min_conflicts(csp, n, board, MAX_STEPS=100):
    """
    ------------------------------------------------------
    Min-Conflicts algorithm for solving CSPs by
    local search.
    ------------------------------------------------------
    Inputs:
        csp - a CSP with components(X, D, C)
        n - the number of queens
        board - the board object keeping track of the
            queens in conflict
        MAX_STEPS - the number of steps allowed before
            giving up
    Returns:
        A solution or failure (False)
    ------------------------------------------------------
    """
    # Tabu Search list and variable to avoid repeating moves
    last_variable = {}
    last_queen = None

    # sets the size of the tabu list
    x = 50 if n >= 100 else (n//2)

    for i in range(1, MAX_STEPS+1):
        # get the list of conflcited queens from the csp constraints
        list_conflicted = [i for i, j in csp.constraints.items() if j != 0]

        # if there are not more conflicting queens then the problem is sovled
        if list_conflicted == []:
            print('Steps: {}'.format(i))
            return csp

        # remove the past queen from the search space
        if last_queen is not None and last_queen in list_conflicted:
            list_conflicted.remove(last_queen)

        # get a random queen from the conflicted list and remove it from the board
        var = choice(list_conflicted)
        last_queen = var
        board.delete_queen(var, csp.domains[var], csp.constraints)

        # set the queens position into the tabu list so we dont place it back here
        if var in last_variable:
            if csp.domains[var] not in last_variable[var]:
                last_variable[var].append(csp.domains[var])
        else:
            last_variable[var] = [csp.domains[var]]

        # get the position with the least conflicts
        value = conflicts(var, csp.domains[var], n, csp, last_variable[var], board)
        if len(last_variable[var]) >= x: last_variable[var].pop(0)

        # set the queen back on the board
        csp.domains[var] = value
        board.set_queen(var, value, csp.constraints)
    return False


def get_least_conflicts_y(x, n, pos_move, board):
    """
    ------------------------------------------------------
    Get's the position with the least conflicts for the
    column.
    ------------------------------------------------------
    Inputs:
        x - the x value on the board
        n - the number of queens
        pos_move - the list of pos_move moves
        board - the board object keeping track of the
            queens in conflict
    Returns:
        y - the y value on the board
    ------------------------------------------------------
    """
    # the list of y values that have the least conflicts
    conflict_list, min_count = [pos_move[0]], board.get_num_conflicts(x, pos_move[0])

    # for the rest of the column find the positions with the least conflicts
    for i in pos_move[1:]:
        count = board.get_num_conflicts(x, i)
        # update the min_count and list
        if min_count > count:
            min_count = count
            conflict_list = [i]
        elif min_count == count:
            conflict_list.append(i)

    return choice(conflict_list)


def conflicts(var, v, n, csp, not_possible, board):
    """
    ------------------------------------------------------
    Get's the position with the least conflicts for the
    column.
    ------------------------------------------------------
    Inputs:
        var - the x value on the board (or queen)
        v - the current y value
        n - the number of queens
        csp - a CSP with components(X, D, C)
        not_possible - the tabu list for that column
        board - the board object keeping track of the
            queens in conflict
    Returns:
        y - the y value on the board
    ------------------------------------------------------
    """
    x, y = var, v
    conflict_list, min_count = [], None

    # check the column for the position with the least conflicts
    for i in range(1, n+1):
        # skip the position we just came from
        if i == y: continue
        count = board.get_num_conflicts(x, i)
        if min_count is not None and min_count > count:
            min_count = count
            conflict_list = [i]
        elif min_count is not None and min_count == count:
            conflict_list.append(i)
        elif min_count is None:
            min_count = count
            conflict_list = [i]

    clist = list(set(conflict_list) - set(not_possible))

    # if the conflict is has positions that the queen has not been to yet
    if clist != []:
        return choice(clist)

    # if there were no positions available for the queen choose a random one
    return choice(conflict_list)


# functions to create and print the board
def create_board(n):
    return [['-' for i in range(n)] for j in range(n)]


def print_board(board):
    for x in board:
        for y in x:
            print(y, end=' ')
        print()
    return