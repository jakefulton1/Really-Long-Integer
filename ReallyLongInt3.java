public class ReallyLongInt3 extends LinkedListPlus2<Integer> implements Comparable<ReallyLongInt3>
{
    private ReallyLongInt3()
    {
        super();
    }

    public ReallyLongInt3(String s)
    {
        super();
        if (s.substring(0, 1).equals("-")) //constructs differently if the num is negative
        {
            int negative = 0 - Integer.valueOf(s.substring(1, 2));
            this.add(1, negative);
            String newS = s.substring(2, s.length());
            strConstructorRec(newS, 0);
        }
        else //constructs normally for positives
        {
            strConstructorRec(s, 0);
            int digit = s.charAt(s.length() - 1)  - '0';
            if (digit == 0 && numberOfEntries == 0)
			this.add(1, Integer.valueOf(digit));
        }
    }

    private void strConstructorRec(String s, int count)
    {
        if (count != s.length())
        {
            int digit = -1;
            char c = s.charAt(count);
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

    public ReallyLongInt3(ReallyLongInt3 rightOp)
	{
		super(rightOp);
	}
    
    public ReallyLongInt3(long X)
    {
        if (X == 0L)
		{
			this.add(0);
		}
        if (X < 0) //If the num is negative
            negativeLongConstructor(X); 
        else //If the num is positive
            longConstructorRec(X);
    }

    private void longConstructorRec(long X)
    {
        if (X != 0)
        { 
            this.add(Math.abs((int)X % 10)); //Adds the last digit of the number
            X = X / 10; //Removes the last digit of the number
            longConstructorRec(X);
        }
    }
    private void negativeLongConstructor(long X)
    {
        if (X != 0 && X < -10) 
        { 
            this.add(Math.abs((int)X % 10)); //Adds the last digit of the number
            X = X / 10; //Removes the last digit of the number
            negativeLongConstructor(X);
        }
        else if(X != 0)
        {
            this.add((int)X % 10);
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

    public ReallyLongInt3 add(ReallyLongInt3 rightOp)
    {
        // if a number is negative, (the most significant figure).data will be negative
        ReallyLongInt3 sum = new ReallyLongInt3();
        if (this.firstNode.prev.data >= 0 && rightOp.firstNode.prev.data >= 0) //pos + pos
        { //example: 1+10               (normal case with no negatives)
            if (this.numberOfEntries >= rightOp.numberOfEntries)
                return addRecur(sum, this.firstNode, rightOp.firstNode, 0, this.numberOfEntries, rightOp.numberOfEntries, 0);
            else
                return addRecur(sum, rightOp.firstNode, this.firstNode, 0, rightOp.numberOfEntries, this.numberOfEntries, 0);
        }
        else if (this.firstNode.prev.data < 0 && rightOp.firstNode.prev.data < 0) //neg + neg
        { //example -1 + -10 = -(abs(-1) + abs(-10))
            if (this.numberOfEntries >= rightOp.numberOfEntries)
                sum =  addRecur(sum, this.firstNode, rightOp.firstNode, 0, this.numberOfEntries, rightOp.numberOfEntries, 0);
            else
                sum = addRecur(sum, rightOp.firstNode, this.firstNode, 0, rightOp.numberOfEntries, this.numberOfEntries, 0);
            sum.firstNode.prev.data = 0 - sum.firstNode.prev.data;
            return sum;
        }
        ReallyLongInt3 difference = new ReallyLongInt3(this.positiveCopy());
        if (this.positiveCopy().compareTo(rightOp.positiveCopy()) == 1 || this.positiveCopy().compareTo(rightOp.positiveCopy()) == 0) //abs(this) > abs(rightOp)
        {
            if (rightOp.firstNode.prev.data < 0) //pos + neg && abs(pos) >= abs(neg) 
            {    //example: 10 + (-1) = 10 - abs(-1)
                sum = subtractRecur(difference, difference.firstNode, rightOp.firstNode, rightOp.numberOfEntries, 0);//this.subtract(rightOp); //gotta make it subtractRecur so it doesn't go through if statements
                return sum;
            }
            else //neg + pos && abs(neg) > abs(pos) 
            {   //example: -10 + 1 = -(abs(-10) - 1)
                sum = subtractRecur(difference, difference.firstNode, rightOp.firstNode, rightOp.numberOfEntries, 0);
                sum.firstNode.prev.data = 0 - sum.firstNode.prev.data; //make sum negative
                return sum;
            }
        }
        else //abs(this) <= abs(rightOp)
        {
            ReallyLongInt3 rdifference = new ReallyLongInt3(rightOp.positiveCopy());
            if (rightOp.firstNode.prev.data < 0) //pos + neg && abs(pos) <= abs(neg)
            {   //example: 1 + (-10) = -(abs(-10) - 1)
                sum = subtractRecur(rdifference, rdifference.firstNode, this.firstNode, this.numberOfEntries, 0);
                sum.firstNode.prev.data = 0 - sum.firstNode.prev.data; //make sum negative
                return sum;
            }
            else //neg + 10 && abs(neg) <= abs(10)
            {   //example: -1 + 10 = 10 - abs(-1)
                sum = subtractRecur(rdifference, rdifference.firstNode, this.firstNode, this.numberOfEntries, 0);
                return sum;
            }
        }
    }

    private ReallyLongInt3 addRecur(ReallyLongInt3 sum, Node bigCurr, Node smallCurr, int carry, int bigLimit, 
    int smallLimit, int counter)
    {
        if (counter != bigLimit)
        {
            int tinySum = Math.abs(bigCurr.data) + Math.abs(smallCurr.data) + carry;
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
                ReallyLongInt3 zero = new ReallyLongInt3(0);
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

    public ReallyLongInt3 subtract(ReallyLongInt3 rightOp) 
    {
        ReallyLongInt3 difference = new ReallyLongInt3(this);
        ReallyLongInt3 rdifference = new ReallyLongInt3(rightOp); //This will be used if this.compareTo(rightOp) == -1
        if (this.firstNode.prev.data >= 0 && rightOp.firstNode.prev.data >= 0) //Both are positive
        {
            if (this.compareTo(rightOp) == 1 || this.compareTo(rightOp) == 0) //This is the normal case. example: 10 - 1
                return subtractRecur(difference, difference.firstNode, rightOp.firstNode, rightOp.numberOfEntries, 0);
            //pos1 - pos2 && pos2 > pos1 = -(pos2.sub(pos1)) example: 1 - 10 = -(10 - )
            rdifference = subtractRecur(rdifference, rdifference.firstNode, this.firstNode, this.numberOfEntries, 0);
            rdifference.firstNode.prev.data = 0 - rdifference.firstNode.prev.data; //make difference negative
            return rdifference;
        }
        else if (this.firstNode.prev.data < 0 && rightOp.firstNode.prev.data < 0) //Both are negative
        {
            if (this.compareTo(rightOp) == -1) //if this < rightOp, abs(this) > abs(rightOp) since they're negative 
            {   //example: -10 - (-1) = -(10 - 1)
                difference = subtractRecur(difference, difference.firstNode, rightOp.firstNode, rightOp.numberOfEntries, 0);
                difference.firstNode.prev.data = 0 - difference.firstNode.prev.data; //make difference negative
                return difference;
            }
            rdifference = subtractRecur(rdifference, rdifference.firstNode, this.firstNode, this.numberOfEntries, 0);
            return rdifference; //example: -1 - (-10) = 10 - 1
        }
        ReallyLongInt3 sum = new ReallyLongInt3();
        if (rightOp.firstNode.prev.data < 0)
        {
            if (this.numberOfEntries >= rightOp.numberOfEntries)
                return addRecur(sum, this.firstNode, rightOp.firstNode, 0, this.numberOfEntries, rightOp.numberOfEntries, 0);
            else
                return addRecur(sum, rightOp.firstNode, this.firstNode, 0, rightOp.numberOfEntries, this.numberOfEntries, 0);
        } 
        else
        {
            if (this.numberOfEntries >= rightOp.numberOfEntries)
                difference = addRecur(sum, this.firstNode, rightOp.firstNode, 0, this.numberOfEntries, rightOp.numberOfEntries, 0);
            else
                difference = addRecur(sum, rightOp.firstNode, this.firstNode, 0, rightOp.numberOfEntries, this.numberOfEntries, 0);
            difference.firstNode.prev.data = 0 - difference.firstNode.prev.data;
            return difference;
        }
    } 

    private ReallyLongInt3 subtractRecur(ReallyLongInt3 difference, Node thisCurr, Node rightCurr, int limit, int count) 
    {
        if (count != limit)
        {
            if (Math.abs(thisCurr.data) >= Math.abs(rightCurr.data))
                thisCurr.data = Math.abs(thisCurr.data) - Math.abs(rightCurr.data);
            else
            {
                Node nextNode = carrying(thisCurr.next);
                nextNode.data = Math.abs(nextNode.data - 1);
                thisCurr.data = (Math.abs(thisCurr.data) + 10 - Math.abs(rightCurr.data));
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

    public ReallyLongInt3 multiply(ReallyLongInt3 rightOp) //Gonna have to change this to account for negatives
    {
        ReallyLongInt3 product = new ReallyLongInt3(0);
        if (this.firstNode.prev.data < 0 ^ rightOp.firstNode.prev.data < 0)
        { //In this case, only one of the factors is negative, so the product must be made negative
            if (this.numberOfEntries >= rightOp.numberOfEntries)
                product = multiplyRecur(product, this.firstNode, rightOp.firstNode, this.numberOfEntries, rightOp.numberOfEntries, 0);
            else
                product = multiplyRecur(product, rightOp.firstNode, this.firstNode, rightOp.numberOfEntries, this.numberOfEntries, 0);
            product.firstNode.prev.data = 0 - product.firstNode.prev.data;
            return product;
        }
        //For these cases, either both factors are positive or are both negative. Either way the product stays positive
        if (this.numberOfEntries >= rightOp.numberOfEntries)
            return multiplyRecur(product, this.firstNode, rightOp.firstNode, this.numberOfEntries, rightOp.numberOfEntries, 0);
        else
            return multiplyRecur(product, rightOp.firstNode, this.firstNode, rightOp.numberOfEntries, this.numberOfEntries, 0);
    }

    private ReallyLongInt3 multiplyRecur(ReallyLongInt3 product, Node bigNode, Node smallNode, 
    int bigLimit, int smallLimit, int counter)
    {
        if (counter != smallLimit)
        {
            ReallyLongInt3 subProduct = new ReallyLongInt3();
            addZerosToSubProduct(subProduct, counter);

            subProduct = makeSubProduct(subProduct, smallNode.data, bigNode, bigLimit, 0, 0);
            product = product.add(subProduct);
            smallNode = smallNode.next;
            counter++;
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

    private ReallyLongInt3 addZerosToSubProduct(ReallyLongInt3 subProduct, int counter)
    {
        if (counter != 0)
        {
            subProduct.add(0);
            return(addZerosToSubProduct(subProduct, counter - 1));
        }
        else
            return(subProduct);
    }
    private ReallyLongInt3 makeSubProduct(ReallyLongInt3 subProduct, int smallNodeDigit, Node bigNode, int limit, 
    int carry, int index)
    {
        if (index != limit)
        {
            int tinyProduct = Math.abs(smallNodeDigit) * Math.abs(bigNode.data) + carry; 
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

    public ReallyLongInt3 positiveCopy()
    { 
        ReallyLongInt3 thisCopy = new ReallyLongInt3(this);
        if (thisCopy.firstNode.prev.data >= 0) //If the number is positive, just return the copy
            return thisCopy; 
        thisCopy.firstNode.prev.data = Math.abs(thisCopy.firstNode.prev.data); //Otherwise make the copy positive
        return thisCopy; 
    }

    public int compareTo(ReallyLongInt3 rOp)
    {
        if (this.equals(rOp)) //Check if they're equal
			return 0;
        if (this.firstNode.prev.data < 0 && rOp.firstNode.prev.data >= 0) //Check if this is negative and rOp is positive
            return -1;
        else if (this.firstNode.prev.data >= 0 && rOp.firstNode.prev.data < 0) //Check if rOp is negative and this is positive
            return 1;
        else if (this.firstNode.prev.data < 0 && rOp.firstNode.prev.data < 0) //Check if they're both negative
        {
            int result = compareRec(this.firstNode.prev, rOp.firstNode.prev);
            if (result == 1) //Flip the result since they're both negative. example: 400 > 7 but -400 < -7
                return -1;
            return 1; 
        }
        if (this.numberOfEntries < rOp.numberOfEntries) //Check if current has less digits (is less than rOp)
        return -1;
        else if (this.numberOfEntries > rOp.numberOfEntries) //Check if current has more digits (is greater)
            return 1;
        
        return compareRec(this.firstNode.prev, rOp.firstNode.prev); //Normal case: both are positive
    }

    private int compareRec(Node thisCurr, Node rightCurr) 
    { //Starting with the most significant digits, check if one number is greater
        if (Math.abs(thisCurr.data) < Math.abs(rightCurr.data))
			return -1;
        else if (Math.abs(thisCurr.data) > Math.abs(rightCurr.data))
            return 1;
        thisCurr = thisCurr.prev;
        rightCurr = rightCurr.prev;
        return compareRec(thisCurr, rightCurr);
    }

    public boolean equals(Object rightOp)
    {
        if (this.numberOfEntries != ((ReallyLongInt3)rightOp).numberOfEntries && this.firstNode.prev.data != 0)
            return false; //If the 2 nums have varying lengths, they're not equal, unless it's like 0 = 00000000
        if (this.firstNode.prev.data < 0 ^ ((ReallyLongInt3)rightOp).firstNode.prev.data < 0)
            return false; //If either of the nums, but not both (thanks XOR) are negative, then the nums aren't equal
        int limit;
        if (((ReallyLongInt3)rightOp).numberOfEntries > this.numberOfEntries)
            limit = ((ReallyLongInt3)rightOp).numberOfEntries;
        else
            limit = numberOfEntries;
        return equalsRec(this.firstNode, ((ReallyLongInt3)rightOp).firstNode, limit, 0); 
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
