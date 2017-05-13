
public class Main
{
    public static void main(String [] args)
    {
    	TheEconomy s = new TheEconomy();
        Pessimist p = new Pessimist(s);
        Optimist o = new Optimist(s);
        s.attach(p);
        s.attach(o);
        //s.setState("The New iPad is out today");
        s.setState("The Price of gas is at $5.00/gal");
        s.setState("Hey, Its Friday!");
        p.showState();
        o.showState();
    }
    
  }