import java.util.List;

public class Constraint
{

    public Sudoku_Axis[] vars; //variables
    public List<int[]> accp; //set of acceptable values for the variables

    public Constraint(Sudoku_Axis[] vars, List<int[]> accp)
    {
        this.vars = vars;
        this.accp = accp;
    }

    /*
     * Checks if variables contain a combination of values that meet their constraints
     */
    public boolean is_valid(int value)
    {
        for (int value_b : vars[1].get_domain())
        {
            for (int[] r : accp)
            {
                // check if  the two values are an acceptable combination
                if (value == r[0] && value_b == r[1])
                {
                    return true;
                }
            }
        }

        return false;
    }
    
    /*
     * Checks if both variables in the constraint have at least one value in their domain
     */
    public boolean valid_domain()
    {
        if (vars[0].get_domain().size() > 0 && vars[1].get_domain().size() > 0)
        {
            return true;
        }
        
        return false;
    }
    /*
     * Checks if the constraint includes a cell identified by the given key
     */
    public boolean contains_key(String key)
    {
        if (key == vars[0].key_index || key == vars[1].key_index)
        {
            return true;
        }
        
        return false;
    }
}

