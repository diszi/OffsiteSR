package hu.d2.offsitesr.util;



import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import hu.d2.offsitesr.app.singleton.SettingsSingleton;
import hu.d2.offsitesr.ui.model.Asset;
import hu.d2.offsitesr.ui.model.AssetUserCust;
import hu.d2.offsitesr.ui.model.Attachment;
import hu.d2.offsitesr.ui.model.DocLinks;
import hu.d2.offsitesr.ui.model.License;
import hu.d2.offsitesr.ui.model.OwnerHolder;
import hu.d2.offsitesr.ui.model.ServiceRequestEntity;
import hu.d2.offsitesr.ui.model.Task;
import hu.d2.offsitesr.ui.model.Version;
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
			String ownerGroup = getNodeValue(element,"PERSONGROUP");
            String owner = getNodeValue(element,"RESPPARTY");

            ownerList.add(owner);
            ownerGroupList.add(ownerGroup);
		}
		return new OwnerHolder(ownerList,ownerGroupList);
	}

	/**
	 *
	 * @return a list with all ticket
	 */
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



	public static List<DocLinks> transformAttachmentDocLinksList(InputStream inputStream, String doclinksID) throws ParserConfigurationException, IOException, SAXException {
		List<DocLinks> attachmentDocLinksList = new LinkedList<>();
		DocumentBuilder builder = null;
		Document document = null;

		builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		document = builder.parse(inputStream);

		NodeList srNode = document.getElementsByTagName("SR");
		for (int i = 0; i < srNode.getLength(); i++) {
			attachmentDocLinksList = transformTicketAttachmentDocLinks((Element) srNode.item(i), doclinksID);

		}
		System.out.println(" ENTITYMAPPER => "+attachmentDocLinksList.size());
		return attachmentDocLinksList;
	}


	/**
	 *  - get attachment list for ticketID - call when download file
	 */
	public static List<Attachment> transformAttachmentList(InputStream inputStream) throws ParserConfigurationException, IOException, SAXException {
		List<Attachment> attachmentDocLinksList = new LinkedList<>();
		DocumentBuilder builder = null;
		Document document = null;

		builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		document = builder.parse(inputStream);

		NodeList srNode = document.getElementsByTagName("SR");
		for (int i = 0; i < srNode.getLength(); i++) {
			attachmentDocLinksList = transformTicketAttachment((Element) srNode.item(i));
		}
		return attachmentDocLinksList;
	}


	private static List<DocLinks> transformTicketAttachmentDocLinks(Element element,String doclinksID)
	{
		NodeList linkNode = element.getElementsByTagName("DOCLINKS");
		List<DocLinks> attachmentDocLink = new ArrayList<>();
		for (int i=0;i<linkNode.getLength();i++){
			DocLinks attachmentDoclink = transformAttachmentDocLinks((Element) linkNode.item(i),doclinksID);
			attachmentDocLink.add(attachmentDoclink);
			//System.out.println(" ---> Doclinks = "+doclinksID+" - "+attachmentDoclink.getDoclinksID()+" - "+attachmentDoclink.getWebURL());
			if (attachmentDoclink.getDocumentData() != null){
			//	System.out.println(" -> getDocumentData != null  ---> "+attachmentDocLink.size());
				return attachmentDocLink;
			}
		}
		return attachmentDocLink;

	}



	private static DocLinks transformAttachmentDocLinks(Element element,String doclinksID){
		DocLinks attachmentDoc = new DocLinks();
		attachmentDoc.setDoclinksID(getNodeValue(element,"DOCLINKSID"));
		if (attachmentDoc.getDoclinksID().equals(doclinksID))
		{
			attachmentDoc.setDocumentData(getNodeValue(element,"DOCUMENTDATA"));
			attachmentDoc.setWebURL(getNodeValue(element,"WEBURL"));
		}
		//System.out.println(" ---- Doclink = "+attachmentDoc.getDoclinksID()+" ??? "+doclinksID+" ---> "+attachmentDoc.getWebURL());
		return attachmentDoc;
	}


	private static List<Attachment> transformTicketAttachment(Element element)
	{
		NodeList linkNode = element.getElementsByTagName("DOCLINKS");
		List<Attachment> attachmentDocLink = new ArrayList<>();
		for (int i=0;i<linkNode.getLength();i++){
			Attachment attachmentDoclink = transformAttachment((Element) linkNode.item(i));
			attachmentDocLink.add(attachmentDoclink);
		}
		return attachmentDocLink;
	}

	private static Attachment transformAttachment (Element element){
		Attachment attachment = new Attachment();
		attachment.setReference(getNodeValue(element,"REFERENCE"));
		attachment.setCreateDate(getNodeValue(element,"CREATEDATE"));
		attachment.setCreateBy(getNodeValue(element,"CREATEBY"));
		attachment.setWebURL(getNodeValue(element,"WEBURL"));
		attachment.setDoclinksID(getNodeValue(element,"DOCLINKSID"));
		attachment.setDescription(getNodeValue(element,"DESCRIPTION"));
		return attachment;
	}


	/**
	 * 	- get Worklog list for recordkey
	 */
	public static List<WorkLog> transformWorkLogList(InputStream inputStream) throws ParserConfigurationException, IOException, SAXException{
		List<WorkLog> workLogList = new LinkedList<>();
		DocumentBuilder builder = null;
		Document document = null;

		builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		document = builder.parse(inputStream);

		NodeList wlNode = document.getElementsByTagName("WORKLOG");
		for (int i = 0; i < wlNode.getLength(); i++) {
			WorkLog worklogObj = transformWorkLogRefresh((Element) wlNode.item(i));
			workLogList.add(worklogObj);
		}
		return workLogList;
	}

	private static WorkLog transformWorkLogRefresh(Element element){
		WorkLog workLog = new WorkLog();
		workLog.setRecordKey(getNodeValue(element, "RECORDKEY"));
		workLog.setCreatedBy(getNodeValue(element, "CREATEBY"));
		workLog.setCreatedDate(getNodeValue(element, "CREATEDATE"));
		workLog.setDescription(getNodeValue(element, "DESCRIPTION"));
		workLog.setLongDescription(getNodeValue(element,"DESCRIPTION_LONGDESCRIPTION"));

		return workLog;

	}

	public static List<License> transformLicenseData(InputStream inputStream, String IMEInumber)
			throws ParserConfigurationException, IOException, SAXException {

		List<License> licenseList = new LinkedList<>();
		DocumentBuilder builder = null;
		Document document = null;

		builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		document = builder.parse(inputStream);

		NodeList licenseNode = document.getElementsByTagName("MOB_LIC1");
		for (int i = 0; i < licenseNode.getLength(); i++) {
			License dataLicense = transformData((Element) licenseNode.item(i));
			if (dataLicense.getPersonID().equals(SettingsSingleton.getInstance().getUserName().toUpperCase())){
				licenseList.add(dataLicense);
			}
		}

		return licenseList;
	}

	private static License transformData(Element element){
		License licenseObj = new License();
		licenseObj.setIMEInumber(getNodeValue(element,"IMEI"));
		licenseObj.setPersonID(getNodeValue(element,"PERSONID"));
		return licenseObj;
	}

	/**
	 * 	- return Version properties with value
	 */
	public static Version transformVersionData(InputStream inputStream) throws ParserConfigurationException, IOException, SAXException {
		Version versionObj = new Version();
		DocumentBuilder builder = null;
		Document document = null;

		builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		document = builder.parse(inputStream);

		NodeList versionUpdate1Node = document.getElementsByTagName("MOB_UPDATE");
		for (int i = 0; i <  versionUpdate1Node.getLength(); i++) {
			Version version = transformVersion((Element) versionUpdate1Node.item(i));

			// if OFFICIAL && D2
			if (version.getMode().equals(UIConstans.VERSION_MODE) && version.getCustomer().equals(UIConstans.VERSION_CUSTOMER)){
				versionObj = version;
			}
		}

		return versionObj;
	}



	private static Version transformVersion(Element element){
		Version versionInfo = new Version();
		versionInfo.setAppName(getNodeValue(element,"APPNAME"));
		versionInfo.setVersionNumber(getNodeValue(element,"VERSION"));
		versionInfo.setMode(getNodeValue(element,"MODE"));
		versionInfo.setCustomer(getNodeValue(element,"CUSTOMER"));

		NodeList newAppNode = element.getElementsByTagName("DOCLINKS");
		List<DocLinks> newAppList = new ArrayList<>();
		for (int i = 0; i < newAppNode.getLength(); i++) {
			DocLinks doclinks = transformDocumentData((Element) newAppNode.item(i));
			newAppList.add(doclinks);
		}

		versionInfo.setNewAppDetails(newAppList);
		return versionInfo;
	}


	private static DocLinks transformDocumentData(Element element){
		DocLinks docLinks = new DocLinks();
		docLinks.setDocumentData(getNodeValue(element,"DOCUMENTDATA"));

		return docLinks;

	}

	/**
	 *  - set all ServiceRequestEntity obj properties
	 */
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

		NodeList dNode = element.getElementsByTagName("DOCLINKS");
		List<Attachment> attachments = new ArrayList<>();
		for (int i=0;i<dNode.getLength();i++){
			Attachment attachment = transformAttachmentDetails((Element) dNode.item(i));
			attachments.add(attachment);
		}
		ticket.setAttachments(attachments);

		return ticket;
	}

	/**
	 *
	 * - set Attachment obj properties
	 */
	private static Attachment transformAttachmentDetails(Element element){
		Attachment attachment = new Attachment();
		attachment.setCreateDate(getNodeValue(element,"CREATEDATE"));
		attachment.setCreateBy(getNodeValue(element,"CREATEBY"));
		attachment.setWebURL(getNodeValue(element,"WEBURL"));
		attachment.setDoclinksID(getNodeValue(element,"DOCLINKSID"));
		attachment.setReference(getNodeValue(element,"REFERENCE"));
		attachment.setDescription(getNodeValue(element,"DESCRIPTION"));
		return attachment;
	}

	/**
	 *
	 * - set Worklog obj properties
	 */
    private static WorkLog transformWorkLog(Element element) {
        WorkLog workLog = new WorkLog();
		workLog.setId(getNodeValue(element,"WORKLOGID"));
        workLog.setCreatedBy(getNodeValue(element, "CREATEBY"));
        workLog.setCreatedDate(getNodeValue(element, "CREATEDATE"));
        workLog.setLogType(getNodeValue(element, "LOGTYPE"));
        workLog.setRecordKey(getNodeValue(element, "RECORDKEY"));
        workLog.setDescription(getNodeValue(element, "DESCRIPTION"));
		workLog.setLongDescription(getNodeValue(element,"DESCRIPTION_LONGDESCRIPTION"));
        return workLog;
    }

	/**
	 *
	 * - set Task obj properties
	 */
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
		task.setSiteId(getNodeValue(element,"SITEID"));
		return task;
	}

	/**
	 *
	 * - set Asset obj properties
	 */
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
