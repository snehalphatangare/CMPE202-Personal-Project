import java.util.Stack;

public class MessageIdStack<E> extends Stack<E>{
	
	public String printAll()
	{
		int len = elementData.length;
		len = findIndex(len);
		return combineMsgNumbers(len);
	}

	private String combineMsgNumbers(int len) {
		String resultMsgNumber= new String();
		for(int i=0;i<len;i++){
			resultMsgNumber+=elementData[i];
			if(i!=(len-1)) {
				resultMsgNumber+=".";
			}
		}
		return resultMsgNumber;
	}

	private int findIndex(int len) {
		for(int i=0;i<len;i++){
			if(elementData[i] == null)
			{
				len =i;
				break;
			}
		}
		return len;
	}
}
