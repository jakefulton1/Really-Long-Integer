public class ReallyLongInt2 extends LinkedListPlus2<Integer> implements Comparable<ReallyLongInt2>
{
    private ReallyLongInt2()
    {
        super();
    }

    public ReallyLongInt2(String s)
    {
        super();
        strConstructorRec(s, 0);
        int digit = s.charAt(s.length() - 1)  - '0';
        if (digit == 0 && numberOfEntries == 0)
			this.add(1, Integer.valueOf(digit));
    }

    private void strConstructorRec(String s, int count)
    {
        if (count != s.length())
        {
            char c; //can remove this line and initialize c when it's assigned
            int digit = -1;
            c = s.charAt(count);
            if (('0' <= c) && (c <= '9'))
            {
                digit = c - '0';
                // Do not add leading 0s
                if (!(digit == 0 && this.getLength() == 0)) 
                    this.add(1, Integer.valueOf(digit));
            }
            else throw new NumberFormatException("Illegal digit " + c);
            strConstructorRec(s, count + 1);
        }
    }

    public ReallyLongInt2(ReallyLongInt2 rightOp)
	{
		super(rightOp);
	}

    public ReallyLongInt2(long X)
    {
        if (X == 0L)
		{
			this.add(0);
		}
        longConstructorRec(X);
    }

    private void longConstructorRec(long X)
    {
        if (X != 0)
        { 
            this.add((int)X % 10); //Adds the last digit of the number
            X = X / 10; //Removes the last digit of the number
            longConstructorRec(X);
        }
    }

    public String toString()
	{
		StringBuilder str = new StringBuilder();
        str = toStrRec(str, firstNode.prev);
        return str.toString();
    }

    private StringBuilder toStrRec(StringBuilder str, Node currNode)
    {
        if (!currNode.equals(firstNode))
        {
            str.append(currNode.data.toString());
            currNode = currNode.prev;
            toStrRec(str, currNode);
        }
        else
            str.append(currNode.data.toString());
        return str;
    }

    public ReallyLongInt2 add(ReallyLongInt2 rightOp)
    {
        ReallyLongInt2 sum = new ReallyLongInt2();
        if (this.numberOfEntries >= rightOp.numberOfEntries)
            return addRecur(sum, this.firstNode, rightOp.firstNode, 0, this.numberOfEntries, rightOp.numberOfEntries, 0);

        else
            return addRecur(sum, rightOp.firstNode, this.firstNode, 0, rightOp.numberOfEntries, this.numberOfEntries, 0);
    }

    private ReallyLongInt2 addRecur(ReallyLongInt2 sum, Node bigCurr, Node smallCurr, int carry, int bigLimit, 
    int smallLimit, int counter)
    {
        if (counter != bigLimit)
        {
            int tinySum = bigCurr.data + smallCurr.data + carry;
			if (tinySum < 10)
			{
				sum.add(tinySum);
				carry = 0;
			}	
			else
			{
				sum.add(tinySum % 10);
				carry = tinySum / 10;
			}
            counter++;
            bigCurr = bigCurr.next; //Increment the node of the number with more digits
            if (counter < smallLimit)
                smallCurr = smallCurr.next; //Increment the node of the number with less digits
            else if (counter == smallLimit)
            { //If the end of the smaller number is reached, replace smallCurr with a Node containing 0
                ReallyLongInt2 zero = new ReallyLongInt2(0);
                smallCurr = zero.firstNode;
            }
            return addRecur(sum, bigCurr, smallCurr, carry, bigLimit, smallLimit, counter);
        }
        else
        {
            if (carry != 0)
			    sum.add(carry);
            return sum;
        }
    }

    public ReallyLongInt2 subtract(ReallyLongInt2 rightOp)
    {
        if (this.compareTo(rightOp) == -1)
			throw new ArithmeticException(rightOp.toString() + " > " + this.toString() + ": Cannot subtract");
        ReallyLongInt2 difference = new ReallyLongInt2(this);
        return subtractRecur(difference, difference.firstNode, rightOp.firstNode, rightOp.numberOfEntries, 0);
    }

    private ReallyLongInt2 subtractRecur(ReallyLongInt2 difference, Node thisCurr, Node rightCurr, int limit, int count) 
    {
        if (count != limit)
        {
            if (thisCurr.data >= rightCurr.data)
                thisCurr.data = thisCurr.data - rightCurr.data;
            else
            {
                Node nextNode = carrying(thisCurr.next);
                nextNode.data = nextNode.data - 1;
                thisCurr.data = (thisCurr.data + 10 - rightCurr.data);
            }
            thisCurr = thisCurr.next;
            rightCurr = rightCurr.next;
            return subtractRecur(difference, thisCurr, rightCurr, limit, count + 1);
        }
        else
        {
            difference.removeLeadZeroes(difference.firstNode.prev);
            return difference;
        }   
    }

    private Node carrying(Node nextNode) 
//If it's like 3001 minus 2, iterate back from 1 until 3. Make all of the 0s 9s
    {
        if (nextNode.data != 0)
            return nextNode;
        nextNode.data = 9;
        nextNode = nextNode.next;
        return carrying(nextNode);
    }

    public ReallyLongInt2 multiply(ReallyLongInt2 rightOp)
    {
        ReallyLongInt2 product = new ReallyLongInt2(0);
        if (this.numberOfEntries >= rightOp.numberOfEntries)
            return multiplyRecur(product, this.firstNode, rightOp.firstNode, this.numberOfEntries, rightOp.numberOfEntries, 0);
        else
            return multiplyRecur(product, rightOp.firstNode, this.firstNode, rightOp.numberOfEntries, this.numberOfEntries, 0);
    }

    private ReallyLongInt2 multiplyRecur(ReallyLongInt2 product, Node bigNode, Node smallNode, 
    int bigLimit, int smallLimit, int counter)
    {
        if (counter != smallLimit)
        {
            ReallyLongInt2 subProduct = new ReallyLongInt2();
            addZerosToSubProduct(subProduct, counter);

            subProduct = makeSubProduct(subProduct, smallNode.data, bigNode, bigLimit, 0, 0);
            //System.out.println("sub: " + subProduct + " len: " + subProduct.numberOfEntries);
            product = product.add(subProduct);
            smallNode = smallNode.next;
            counter++;
            //System.out.println("main: " + product + " len: " + product.numberOfEntries);
            //increment everything
            return multiplyRecur(product, bigNode, smallNode, bigLimit, smallLimit, counter);
        }
        else
        {
            product.removeLeadZeroes(product.firstNode.prev);
                return product;
             //This way if product is like 00000 it will stay as just 0
        }
    }

    private ReallyLongInt2 addZerosToSubProduct(ReallyLongInt2 subProduct, int counter)
    {
        if (counter != 0)
        {
            subProduct.add(0);
            return(addZerosToSubProduct(subProduct, counter - 1));
        }
        else
            return(subProduct);
    }
    private ReallyLongInt2 makeSubProduct(ReallyLongInt2 subProduct, int smallNodeDigit, Node bigNode, int limit, 
    int carry, int index)
    {
        if (index != limit)
        {
            int tinyProduct = smallNodeDigit * bigNode.data + carry; 
            subProduct.add(tinyProduct % 10);
            carry = tinyProduct / 10;
            bigNode = bigNode.next;
            index++;
            return (makeSubProduct(subProduct, smallNodeDigit, bigNode, limit, carry, index));
        }
        else
        {
            if (carry != 0)
                subProduct.add(carry);
            return subProduct;
        }
    }

    public void removeLeadZeroes(Node node)
    {
        if (node.data == 0 && numberOfEntries != 0)
        {
            this.firstNode.prev = node.prev;
			node.prev.next = this.firstNode;
			node = node.prev;
			this.numberOfEntries--;
            removeLeadZeroes(node);
        }
        else if (numberOfEntries == 0)
            this.add(0);
    }

    public int compareTo(ReallyLongInt2 rOp)
    {
        if (this.equals(rOp)) //Check if they're equal
			return 0;
        if (this.numberOfEntries < rOp.numberOfEntries) //Check if current has less digits (is less than rOp)
        return -1;
        else if (this.numberOfEntries > rOp.numberOfEntries) //Check if current has more digits (is greater)
            return 1;
        return compareRec(this.firstNode.prev, rOp.firstNode.prev); 
    }

    private int compareRec(Node thisCurr, Node rightCurr) 
    { //Starting with the most significant digits, check if one number is greater
        if (thisCurr.data < rightCurr.data)
			return -1;
        else if (thisCurr.data > rightCurr.data)
            return 1;
        thisCurr = thisCurr.prev;
        rightCurr = rightCurr.prev;
        return compareRec(thisCurr, rightCurr);
    }

    public boolean equals(Object rightOp)
    {
        if (this.numberOfEntries != ((ReallyLongInt2)rightOp).numberOfEntries && this.firstNode.prev.data != 0)
            return false; //If the 2 nums have varying lengths, they're not equal, unless it's like 0 = 00000000
        int limit;
        if (((ReallyLongInt2)rightOp).numberOfEntries > numberOfEntries)
            limit = ((ReallyLongInt2)rightOp).numberOfEntries;
        else
            limit = numberOfEntries;
        return equalsRec(firstNode, ((ReallyLongInt2)rightOp).firstNode, limit, 0); 
        }
    
        private boolean equalsRec(Node thisCurr, Node rightCurr, int limit, int count)
        {
            if (count < limit)
            {
                if (thisCurr.data != rightCurr.data)
                    return false;
                thisCurr = thisCurr.next;
                rightCurr = rightCurr.next;
                count++;
                return equalsRec(thisCurr, rightCurr, limit, count);
            }
            return true;
        }
}
