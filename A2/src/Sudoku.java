
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Sudoku
{

    Sudoku_Axis[][] table = new Sudoku_Axis[9][9];
    CSP_Algorithm csp;

    public Sudoku()
    {
        Sudoku_Maker();
        System.out.println("Input:");
        print_sudoku();
        Csp_Maker();
        System.out.println("Applying AC-3 Algorithm");
        csp.AC3_Algorithm();
        /* use backtracking to fix */
        if (!csp.is_complete())
        {
            System.out.println("AC-3 Algorithm complete, but Sudoku not yet solved\nApplying Backtrack Search");
            csp.backtrack();
        }
        else
        {
            System.out.println("Sudoku solved by AC-3 Algorithm");
        }
        System.out.println("Output:");
        print_sudoku();

    }
    /*
     * CSP get's created
     */
    public void Csp_Maker()
    {
        /* create the domain */
        int[] domain =
        {
            1, 2, 3, 4, 5, 6, 7, 8, 9
        };
        /* create the constraints */
        List<Constraint> constraints = new ArrayList<Constraint>();
        List<int[]> binary_domain = new ArrayList<int[]>();

        /* create the binary domain for two values that can't be equal */
        for (int i = 1; i <= 9; i++)
        {
            for (int j = 1; j <= 9; j++)
            {
                if (i != j)
                {
                    int[] temp = new int[2];
                    temp[0] = i;
                    temp[1] = j;
                    binary_domain.add(temp);
                }
            }
        }

        /* create the binary constraints between a pair of cells */
        for (int ya = 0; ya < 9; ya++)
        {
            for (int xa = 0; xa < 9; xa++)
            {
                for (int yb = 0; yb < 9; yb++)
                {
                    for (int xb = 0; xb < 9; xb++)
                    {
                        if ((xa != xb || ya != yb) //don't need to check the cell against itself
                                && table[xa][ya].get_domain().size() > 1 //don't need to check filled in cell
                                && (xa == xb || ya == yb || (xa / 3 == xb / 3 && ya / 3 == yb / 3))) //if two cells share a constraint
                        {
                            /* select the pair of cells */
                            Sudoku_Axis[] temp = new Sudoku_Axis[2];
                            temp[0] = table[xa][ya];
                            temp[1] = table[xb][yb];

                            /* create the constraints */
                            constraints.add(new Constraint(temp, binary_domain));
                        }
                    }
                }
            }
        }

        /* create the CSP */
        csp = new CSP_Algorithm(table, domain, constraints);
    }
    /**
     * Opens a text file to read a sudoku table from and stores the table in an array
     */
    private void Sudoku_Maker()
    {
        String filename = "";
        Scanner r;
        BufferedReader filereader;
        boolean open_file = false;
        /*check file*/
        while (!open_file)
        {
            open_file = true;
            try
            {
                r = new Scanner(System.in);
                System.out.println("Enter the text file's name: ");
                filename = r.next();
                
                if (filename.substring(0,1).equals("\""))
                {
                    filename = filename.substring(1,filename.length() - 1);
                }
                filereader = new BufferedReader(new FileReader(filename));                
                String line;
                int count_line = 0;
                while ((line = filereader.readLine()) != null && count_line < 9)
                {
                    for (int i = 0; i < 9; i++)
                    {
                        if (Character.isDigit(line.charAt(i)) && Integer.parseInt(String.valueOf(line.charAt(i))) > 0)
                        {
                            table[i][count_line] = new Sudoku_Axis(count_line, i, Integer.parseInt(String.valueOf(line.charAt(i))));
                        }
                        else
                        {
                            table[i][count_line] = new Sudoku_Axis(count_line, i);
                        }
                    }
                    count_line++;
                }

                filereader.close();
            }
            catch (Exception e)
            {
                System.out.format("Error: unable to open '%s'.\n", filename);
                open_file = false;
             }
         }
     }

    /*print sudoku table*/
     public void print_sudoku()
     {
         for (int i = 0; i < 9; i++)
         {
             for (int j = 0; j < 9; j++)
             {
                 System.out.print(table[j][i].toString());
             }
             System.out.print("\n"); 
         }
     }
 }


