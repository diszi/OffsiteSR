package hu.d2.offsitesr.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import hu.d2.offsitesr.ui.model.Asset;
import hu.d2.offsitesr.ui.model.AssetUserCust;
import hu.d2.offsitesr.ui.model.OwnerHolder;
import hu.d2.offsitesr.ui.model.ServiceRequestEntity;
import hu.d2.offsitesr.ui.model.Task;
import hu.d2.offsitesr.ui.model.WorkLog;

/**
 * Created by csabinko on 2017.09.15..
 */

public class EntityMapper {

	public static OwnerHolder transformOwnerDataList(InputStream inputStream)
			throws ParserConfigurationException, IOException, SAXException {

		List<String> ownerList = new ArrayList<>();
        List<String> ownerGroupList = new ArrayList<>();

		DocumentBuilder builder = null;
		Document document = null;

		builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		document = builder.parse(inputStream);

		NodeList srNode = document.getElementsByTagName("PERSONGROUPTEAM");
		for (int i = 0; i < srNode.getLength(); i++) {
			Element element =(Element) srNode.item(i);
			String ownerGroup = getNodeValue(element,"RESPPARTYGROUP");
            String owner = getNodeValue(element,"RESPPARTY");
            ownerList.add(owner);
            ownerGroupList.add(ownerGroup);
		}
		return new OwnerHolder(ownerList,ownerGroupList);
	}

	public static List<ServiceRequestEntity> transformTicketList(InputStream inputStream)
			throws ParserConfigurationException, IOException, SAXException {

		List<ServiceRequestEntity> ticketList = new LinkedList<>();
		DocumentBuilder builder = null;
		Document document = null;

		builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		document = builder.parse(inputStream);

		NodeList srNode = document.getElementsByTagName("SR");
		for (int i = 0; i < srNode.getLength(); i++) {
			ServiceRequestEntity dataTicket = transformTicket((Element) srNode.item(i));
			ticketList.add(dataTicket);
		}

		return ticketList;
	}



	private static ServiceRequestEntity transformTicket(Element element) {
		ServiceRequestEntity ticket = new ServiceRequestEntity();

		ticket.setAffectedPerson(getNodeValue(element, "AFFECTEDPERSON"));
		ticket.setTicketClass(getNodeValue(element, "CLASS"));
		ticket.setDescription(getNodeValue(element, "DESCRIPTION"));
		ticket.setOwnerGroup(getNodeValue(element, "OWNERGROUP"));
		ticket.setOwner(getNodeValue(element, "OWNER"));
		ticket.setReportDate(getNodeValue(element,"REPORTDATE"));
		ticket.setReportedBy(getNodeValue(element, "REPORTEDBY"));
		ticket.setStatus(getNodeValue(element, "STATUS"));
		ticket.setAssetNum(getNodeValue(element, "ASSETNUM"));
		ticket.setCINum(getNodeValue(element, "CINUM"));
		ticket.setStatusDate(getNodeValue(element, "STATUSDATE"));
		ticket.setTicketId(getNodeValue(element, "TICKETID"));
		ticket.setClassStructure(getNodeValue(element, "CLASSSTRUCTUREID"));
		ticket.setPriority(getNodeValue(element, "INTERNALPRIORITY"));

		NodeList aNode = element.getElementsByTagName("ASSET");
		Asset asset = transformAsset((Element) aNode.item(0));

		ticket.setAsset(asset);

        NodeList wlNode = element.getElementsByTagName("WORKLOG");
        List<WorkLog> workLogs = new ArrayList<>();
        for (int i = 0; i < wlNode.getLength(); i++) {
            WorkLog workLog = transformWorkLog((Element) wlNode.item(i));
            workLogs.add(workLog);
        }
        ticket.setWorkLogs(workLogs);

		NodeList tNode = element.getElementsByTagName("WOACTIVITY");
		List<Task> tasks = new ArrayList<>();
		for (int i = 0; i < tNode.getLength(); i++) {
			Task task = transformTask((Element) tNode.item(i));
			tasks.add(task);
		}
		ticket.setTasks(tasks);
		return ticket;
	}

    private static WorkLog transformWorkLog(Element element) {
        WorkLog workLog = new WorkLog();
		workLog.setId(getNodeValue(element,"WORKLOGID"));
        workLog.setCreatedBy(getNodeValue(element, "CREATEBY"));
        workLog.setCreatedDate(getNodeValue(element, "CREATEDATE"));
        workLog.setLogType(getNodeValue(element, "LOGTYPE"));
        workLog.setRecordKey(getNodeValue(element, "RECORDKEY"));
        workLog.setDescription(getNodeValue(element, "DESCRIPTION"));
        return workLog;
    }

    private static Task transformTask(Element element){
		Task task = new Task();
		task.setActivity(getNodeValue(element,"WONUM"));
		task.setSummary(getNodeValue(element,"DESCRIPTION"));
		task.setOwner(getNodeValue(element,"OWNER"));
		task.setOwnerGroup(getNodeValue(element,"OWNERGROUP"));
		task.setLocation(getNodeValue(element,"LOCATION"));
		task.setAsset(getNodeValue(element,"ASSETNUM"));
		task.setCi(getNodeValue(element,"CINUM"));
		task.setStatus(getNodeValue(element,"STATUS"));
		return task;
	}

	private  static Asset transformAsset(Element element){
		Asset asset = null;
		if (element != null) {
			asset = new Asset();
			asset.setAssetNum(getNodeValue(element, "ASSETNUM"));
			asset.setDescription(getNodeValue(element, "DESCRIPTION"));
			asset.setStatus(getNodeValue(element, "STATUS"));
			asset.setLocation(getNodeValue(element, "LOCATION"));
			asset.setPluspCustomer(getNodeValue(element, "PLUSPCUSTOMER"));
			asset.setSerialNum(getNodeValue(element, "SERIALNUM"));



			NodeList aUCNode = element.getElementsByTagName("ASSETUSERCUST");
			List<AssetUserCust> assetUserCusts = new ArrayList<>();
			for (int i = 0; i < aUCNode.getLength(); i++) {
				AssetUserCust assetUserCust = transformAssetUserCust((Element) aUCNode.item(i));
				assetUserCusts.add(assetUserCust);
			}
			asset.setAssetUserCustList(assetUserCusts);



		}
		return asset;


	}

	private static AssetUserCust transformAssetUserCust(Element element){
		AssetUserCust assetUserCust=null;
		if (element != null){
			assetUserCust = new AssetUserCust();
			assetUserCust.setIsCustodian(getNodeValueBoolean(element,"ISCUSTODIAN"));
			assetUserCust.setIsUser(getNodeValueBoolean(element,"ISUSER"));
			assetUserCust.setIsPrimary(getNodeValueBoolean(element,"ISPRIMARY"));
			assetUserCust.setLocation(getNodeValue(element,"LOCATION"));
			assetUserCust.setPersonID(getNodeValue(element,"PERSONID"));
		}
		return assetUserCust;
	}

	private static String getNodeValue(Element element, String tag) {

		return element.getElementsByTagName(tag).item(0).getTextContent();
	}


	private  static boolean getNodeValueBoolean(Element element, String tag){
		return element.getElementsByTagName(tag).item(0).getTextContent().equals("1") ;
	}
}
