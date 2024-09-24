public class LinkedListPlus2<T> extends A3LList<T>
{
    public LinkedListPlus2()
    {
        super();
    }

    public LinkedListPlus2(LinkedListPlus2<T> oldList)
    {
        super();
        if (oldList.numberOfEntries > 0)
        {
            int count = 0;
            copyConstructorRec(oldList.firstNode, oldList, count);
        }
    }
    private void copyConstructorRec(Node curr, LinkedListPlus2<T> oldList, int count)
    {
        if (count == oldList.numberOfEntries)
            return;
        else 
        {    
            this.add(curr.data);
            curr = curr.next;
            count++;
            copyConstructorRec(curr, oldList, count); 
        }
    }

    public String toString()
    {
        StringBuilder b = new StringBuilder();
        int count = 0;
        b = toStrRec(b, firstNode, count);
        return b.toString();
    }
    private StringBuilder toStrRec(StringBuilder b, Node curr, int count)
    {
        if (count != numberOfEntries)
        {
            b.append(curr.data.toString());
            b.append(" ");
            curr = curr.next;
            count++;
            toStrRec(b, curr, count);
        }
        return b;
    }

    public void leftShift(int num)
    {
        if (num < 0)
            return;
        else if (num >= numberOfEntries) 
        { //In the case that num > numberOfEntries, the list should become an empty list
            firstNode = null;
            numberOfEntries = 0;
        }
        else
        {
            Node lastNode = firstNode.prev;
            leftShiftRec(num);
            firstNode.prev = lastNode; //Link first and last nodes and decrement numberOfEntries
			lastNode.next = firstNode;
			numberOfEntries -= num;
        }
    }
    private void leftShiftRec(int num)
    {
        if (num != 0)
        {
            firstNode = firstNode.next;
            num--;
            leftShiftRec(num);
        }
    }


    public void rightShift(int num)
    {
        if (num < 0)
            return;
        else if (num >= numberOfEntries) 
        { //In the case that num > numberOfEntries, the list should become an empty list
            firstNode = null;
            numberOfEntries = 0;
        }
        else
        {
            Node lastNode = firstNode.prev;
            lastNode = rightShiftRec(lastNode, num);
            firstNode.prev = lastNode; //Link first and last nodes and decrement numberOfEntries
			lastNode.next = firstNode;
			numberOfEntries -= num;
        }
    }
    private Node rightShiftRec(Node lastNode, int num)
    {
        if (num != 0)
        {
            lastNode = lastNode.prev;
            num--;
            lastNode = rightShiftRec(lastNode, num);
        }
        return lastNode;
    }
    
    public void leftRotate(int num)
    {
		if (num == 0)
			return;
		else if (num < 0)
			rightRotate(Math.abs(num % numberOfEntries));
		else
            leftRotateRec(num % numberOfEntries);
	}
    private void leftRotateRec(int count)
    {
        if (count != 0)
        {
            firstNode = firstNode.next;
            count--;
            leftRotateRec(count);
        }
    }

    public void rightRotate(int num)
    {
		if (num == 0)
			return;
		else if (num < 0)
			leftRotate(Math.abs(num % numberOfEntries));
		else
            rightRotateRec(num % numberOfEntries);
	}
    private void rightRotateRec(int count)
    {
        if (count != 0)
        {
            firstNode = firstNode.prev;
            count--;
            rightRotateRec(count);
        }
    }
}