import java.util.ArrayList;
import java.util.List;

public class CSP_Algorithm
{
    Sudoku_Axis[][] x;
    int[] d;
    List<Constraint> c;

    public CSP_Algorithm(Sudoku_Axis[][] variables, int[] domains, List<Constraint> constraints)
    {
        x = variables;
        d = domains;
        c = constraints;
    }

    /*
     * Applies the AC-3 algorithm to the CSP
     */
    public boolean AC3_Algorithm()
    {
        /* create a queue of all constraints */
        List<Constraint> queue = new ArrayList<Constraint>();
        queue.addAll(c);
        Constraint curr_const;
        while (queue.size() > 0)
        {
            curr_const = queue.remove(0);

            /* Check if any values can be removed from the first variables domain */
            if (curr_const.vars[0].get_domain().size() > 1 && revise(curr_const))
            {
                // if either of the domains are empty, the algorithm fails
                if (!curr_const.valid_domain())
                {
                    return false;
                }

                // since the first variables domain has changed, add all it's neighbours to the queue
                add_neighbour(queue, curr_const.vars[0], curr_const.vars[1]);
            }
        }

        return true;

    }


    /*
     * Gets all constraints that include a given node
     */
    public void add_neighbour(List<Constraint> queue, Sudoku_Axis i, Sudoku_Axis j)
    {
        for (Constraint const_cur : c)
        {
            if (const_cur.contains_key(i.key_index) && !const_cur.contains_key(j.key_index) && !queue.contains(const_cur))
            {
                queue.add(const_cur);
            }
        }
    }
    /**
     * Checks if the CSP has been solved
     * @return true if the CSP has been solved
     */
    public boolean is_complete()
    {
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                if (x[i][j].get_domain().size() != 1)
                {
                    return false;
                }
            }
        }

        return true;
    }
    /*
     * Checks the domain for values that meet the constraints 
     */
    public boolean revise(Constraint current)
    {
        boolean revised = false;
        List<Integer> domain_a = current.vars[0].get_domain();
        int i = 0;

        /* check if any values can be removed from the domain */
        while (i < domain_a.size())
        {
            int value = domain_a.get(i);

            if (!current.is_valid(value))
            {
                current.vars[0].remove_domain(value);
                revised = true;
            }
            else
            {
                i++;
            }
        }

        return revised;
    }


    private boolean cell_consistent(Sudoku_Axis cell)
    {
        for (Constraint current : c)
        {
            if (current.vars[0].key_index == cell.key_index)
            {
                if (revise(current))
                {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Initiates a backtrack search
     * @return true if the search successfully completes the CSP
     */
    public boolean backtrack()
    {
        List<Sudoku_Axis> queue = new ArrayList<Sudoku_Axis>();

        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                if (x[i][j].get_domain().size() > 1)
                {
                    queue.add(x[i][j]);
                }
            }
        }

        return rec_backtrack(queue);
    }
    /**
     * Checks if the domain is valid, i.e. all variables have a domain size > 0
     * @return true if the domain is valid
     */
    private boolean domain_valid()
    {
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                if (x[i][j].get_domain().size() == 0)
                {
                    return false;
                }
            }
        }

        return true;
    }
    /**
     * The recursive component of backtrack search
     * @param queue The list of cells to be included in the search
     * @return true if the selected domain value is correct
     */
    
    private boolean rec_backtrack(List<Sudoku_Axis> queue)
    {
        if (queue.size() == 0)
        {
            if (is_complete())
            {
                return true;
            }
            else
            {
                return false;
            }
        }

        List<Integer> current_domain = new ArrayList<Integer>();

        Sudoku_Axis current = queue.remove(0); 

        current_domain.addAll(current.get_domain());

        /* try every value in the current node's domain */
        for (int i : current_domain)
        {
            current.set_domain(i);

            if (cell_consistent(current) && rec_backtrack(queue))
            {
                return true;
            }
        }
        /* since an acceptable value was not found, reset domain and return false */
        current.set_domain(current_domain);
        queue.add(0, current);
        return false;
    }

}

