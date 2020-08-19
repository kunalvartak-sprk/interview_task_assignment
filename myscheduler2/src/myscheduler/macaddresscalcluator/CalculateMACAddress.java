package myscheduler.macaddresscalcluator;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


public class CalculateMACAddress {

	public List<String> getMACAddress()
	{
		InetAddress ip;
		List<String> mylist = new ArrayList<String>();
		
		try {
				ip = InetAddress.getLocalHost();
				NetworkInterface network = NetworkInterface.getByInetAddress(ip);
				byte[] mac = network.getHardwareAddress();
				//System.out.print("MAC address : ");
	
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < mac.length; i++) 
				{
					sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
					
				}
				mylist.add( sb.toString() );
			}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		//mylist.add("E4-F8-9C-F0-0B-19");
		return mylist;
	}
}
