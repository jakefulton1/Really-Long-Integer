public class ExtraCredRLITest3 
{
    public static void main (String [] args)
	{
        //Testing initialization and printing
        ReallyLongInt3 R1 = new ReallyLongInt3(12345); //Testing long constructor
        ReallyLongInt3 R2 = new ReallyLongInt3(-12345);
		ReallyLongInt3 R3 = new ReallyLongInt3(-804);
        System.out.println(R1 + "\n" + R2 + "\n" + R3);
        R1 = new ReallyLongInt3("54321"); //Testing string constructor
        R2 = new ReallyLongInt3("-54321");
        System.out.println(R1 + "\n" + R2);
        R3 = new ReallyLongInt3(R1); //Testing copy constructor
        R1 = new ReallyLongInt3(R2);
        System.out.println(R3 + "\n" + R1);

        //Testing compareTo 
        ReallyLongInt3 [] A = new ReallyLongInt3[5];
		A[0] = new ReallyLongInt3("1");
		A[1] = new ReallyLongInt3("800");
		A[2] = new ReallyLongInt3("-1");
		A[3] = new ReallyLongInt3("-800");
		A[4] = new ReallyLongInt3("0");
        System.out.println(A[4].compareTo(A[1]));
        for (int i = 0; i < A.length; i++)
		{
			for (int j = 0; j < A.length; j++)
			{
				int ans = A[i].compareTo(A[j]);
				if (ans < 0)
					System.out.println(A[i] + " < " + A[j]);
				else if (ans > 0)
					System.out.println(A[i] + " > " + A[j]);
				else
					System.out.println(A[i] + " == " + A[j]); 
			}                          
		}
        System.out.println();

        //Testing add method
        ReallyLongInt3 [] B = new ReallyLongInt3[5]; //Buncha errors, start debugging lol
        B[0] = new ReallyLongInt3(1234);
		B[1] = new ReallyLongInt3(-1234);
		B[2] = new ReallyLongInt3(876001);
		B[3] = new ReallyLongInt3(-876001);
		B[4] = new ReallyLongInt3(0);
        for (int i = 0; i < B.length; i++)
		{
			for (int j = 0; j < B.length; j++)
			{
				ReallyLongInt3 ans = B[i].add(B[j]);
				System.out.println(B[i] + " + " + B[j] + " = " + ans); 
			}                          
		}
		System.out.println();

        //Testing subtract method
		ReallyLongInt3 [] C = new ReallyLongInt3[5]; //Buncha errors, start debugging lol
        C[0] = new ReallyLongInt3(1234);
		C[1] = new ReallyLongInt3(-1234);
		C[2] = new ReallyLongInt3(876001);
		C[3] = new ReallyLongInt3(-876001);
		C[4] = new ReallyLongInt3(0);
        for (int i = 0; i < C.length; i++)
		{
			for (int j = 0; j < C.length; j++)
			{
				ReallyLongInt3 ans = C[i].subtract(C[j]);
				System.out.println(C[i] + " - " + C[j] + " = " + ans); 
			}                          
		}
		System.out.println();

        //Testing multiply method
        ReallyLongInt3 [] M = new ReallyLongInt3[5]; 
        M[0] = new ReallyLongInt3(12);
		M[1] = new ReallyLongInt3(-12);
		M[2] = new ReallyLongInt3(804);
		M[3] = new ReallyLongInt3(-804);
		M[4] = new ReallyLongInt3(0);
		//System.out.println(M[3]);
        for (int i = 0; i < M.length; i++)
		{
			for (int j = 0; j < M.length; j++)
			{
				ReallyLongInt3 ans = M[i].multiply(M[j]);
				System.out.println(M[i] + " * " + M[j] + " = " + ans); 
			}                          
		}
    }
}
