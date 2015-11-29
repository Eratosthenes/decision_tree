import java.*;
import java.util.*;
import java.util.Scanner;
import java.io.*;

public class DecisionTree
{   
    private static int [] cls = new int[2]; //possible classes are stored in cls[0] and cls[1]
    private static int ncols = 0;

    public static void main(String args[]) throws FileNotFoundException, IOException
    {
        int modeFlag=Integer.parseInt(args[0]);

        BufferedReader r1 = new BufferedReader(new FileReader(args[1]));
        ArrayList<String> examples = new ArrayList<String>();
        String example;
        String first_char;

        //read in examples
        while ((example = r1.readLine()) != null)
        {   if (!example.equals("") && Character.isDigit(example.charAt(0)))
            {   examples.add(example);
            }
        }

        int num_examples = examples.size();
        ncols = examples.get(0).split(",").length;
        HashMap hm = new HashMap(ncols-1);
        BufferedReader r2 = new BufferedReader(new FileReader(args[1]));

        //parse the header
        int key = 0; //attribute key
        while ((example = r2.readLine()) != null && !Character.isDigit(example.charAt(0)))
        {   first_char = example.substring(0,1);
            //store the classes
            if (first_char.equals("%"))
            {   example = example.replace("%",""); 
                String[] s = example.split(",");
                cls[0] = Integer.parseInt(s[0]); 
                cls[1] = Integer.parseInt(s[1]); 
            } 
            //create hashmap for attributes
            if (first_char.equals("#"))
            {   example = example.replace("#",""); 
                example = example.replace(",",""); 
                String[] s = example.split("\\s");
                hm.put(key, s[0]);
                key++;
            }
        }

        //specify the different modes
        switch (modeFlag)
        {   case 0:
                // print info gain for each possible attribute at root node
                //for each possible attribute
                for (int i=0; i<ncols-1; i++) //don't want to include the classification
                {   System.out.println(hm.get(i)+" "+Gain(examples, i));
                }
                break;
            case 1:
                // print decision tree
                System.out.println("Root ");
                //create the attributes array (maybe use arraylist for easier removal?)
                ArrayList<Integer> attributes = new ArrayList<Integer>(ncols-1);
                for (int i=0; i<ncols-1; i++)
                {   attributes.add(i);
                    System.out.println("Attribute "+i+": "+hm.get(attributes.get(i)));
                }
                build_Tree(examples, attributes, examples);
                break;
            case 2:
                // print classifications for all examples in test set
                break;
            default:
                System.out.println("Something went wrong.");
                break;
        }

    }
    private static void build_Tree(ArrayList<String> examples, ArrayList<Integer> attributes, 
        ArrayList<String> default_examples)
    {   
        int num_examples = examples.size();
        String[] example = examples.get(0).split(","); 
        boolean choice_needed=false;

        //determine if there are positive and negative examples
        int Y, num_pos = 0, num_neg = 0;
        for (int i=0; i<num_examples; i++)
        {   example = examples.get(i).split(","); 
            Y = Integer.parseInt(example[ncols-1]);
            num_neg = (Y==cls[0]) ? num_neg+1 : num_neg;
            num_pos = (Y==cls[1]) ? num_pos+1 : num_pos;
            choice_needed = (num_neg>0 && num_pos>0) ? true : false;
        }       

        if (choice_needed==true)
        {   choose_Attribute(examples, attributes);   
            //more code here
        }

        if (num_neg==num_examples)
        {   System.out.print("No");
        }
        if (num_pos==num_examples)
        {   System.out.print("Yes");
        }

        if (num_examples==0)
        {   //choose majority of parent set
        }

        if (attributes.isEmpty()==true)
        {   //choose majority of set of examples
        }
    } 
    private static String choose_Attribute(ArrayList<String> examples, ArrayList<Integer> attributes)
    {
        return null;
    } 
    private static void print_Examples(ArrayList<String> examples)
    {   for (int i=0; i<examples.size(); i++)
        {   System.out.println(examples.get(i));
        }
    }
    //find the gain for a particular attribute
    private static double Gain(ArrayList<String> examples, int attribute)
    {
        int num_examples = examples.size();
        double P_a1, P_a2, P_a3;
        double Gain, Remainder;
        String[] example = examples.get(0).split(","); 
        ArrayList<String> list_a1 = new ArrayList<String>();
        ArrayList<String> list_a2 = new ArrayList<String>();
        ArrayList<String> list_a3 = new ArrayList<String>();

        //for each example, do calculations
        for (int i=0; i<num_examples; i++)
        {   example = examples.get(i).split(","); 
            switch (Integer.parseInt(example[attribute]))
            {   case 1:
                    list_a1.add(examples.get(i));
                    break;
                case 2:
                    list_a2.add(examples.get(i));
                    break;
                case 3:
                    list_a3.add(examples.get(i));
                    break; 
                default:
                    System.out.println("Something went wrong."); 
                    break;
            }
        }
        P_a1 = list_a1.size() / (double) num_examples;
        P_a2 = list_a2.size() / (double) num_examples;
        P_a3 = list_a3.size() / (double) num_examples;

        Remainder = P_a1*I(list_a1) + P_a2*I(list_a2) + P_a3*I(list_a3);
        Gain = I(examples) - Remainder;

        return Gain;
    }
    //calculates entropy
    private static double I(ArrayList<String> examples)
    {   
        int num_examples = examples.size();
        String[] example = examples.get(0).split(","); 
        int Y, num_pos = 0, num_neg = 0;
        double P_pos, P_neg, I;

        for (int i=0; i<num_examples; i++)
        {   example = examples.get(i).split(","); 
            Y = Integer.parseInt(example[ncols-1]);
            num_neg = (Y==cls[0]) ? num_neg+1 : num_neg;
            num_pos = (Y==cls[1]) ? num_pos+1 : num_pos;
        }
        P_neg = num_neg/(double) num_examples;
        P_pos = num_pos/(double) num_examples;
        I = - P_neg*Math.log(P_neg)/Math.log(2) - P_pos*Math.log(P_pos)/Math.log(2);

        return I;
    }
}

