import java.util.ArrayList;
import java.util.List;

public class Sudoku_Axis
{
    public String key_index;
    public int pos_x;
    public int pos_y;
    private List<Integer> domain = new ArrayList<Integer>();
    private List<Constraint> constraints = new ArrayList<Constraint>();
    
    public Sudoku_Axis(int pos_y, int pos_x, int domain)
    {
        key_index = Integer.toString((pos_y * 10) + pos_x);
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.domain.clear();
        this.domain.add(domain);
    }
    
    public Sudoku_Axis(int pos_y, int pos_x)
    {
        key_index = Integer.toString((pos_y * 10) + pos_x);
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        domain.clear();        
        for (int i = 1; i <= 9; i++)
        {
            domain.add(i);
        }
    }
    
    public void set_domain(int value)
    {
        domain.clear();
        domain.add(value);
    }
    
    public void set_domain(List<Integer> values)
    {
        domain.clear();
        domain.addAll(values);
    }
    
    public void remove_domain(int n)
    {
        domain.remove(domain.indexOf(n));
    }
    
    public List<Integer> get_domain()
    {
        return domain;
    }
    @Override
    public String toString()
    {
        if (domain.size() == 1)
        {
            return domain.get(0).toString();
        }
        else if (domain.size() > 1)
        {
            return "0";
        }
        else
        {
            return "X";
        }
    }  
}
