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

import hu.d2.offsitesr.ui.model.ServiceRequestEntity;
import hu.d2.offsitesr.ui.model.WorkLog;

/**
 * Created by csabinko on 2017.09.15..
 */

public class EntityMapper {

	public static List<String> transformOwnerList(InputStream inputStream)
			throws ParserConfigurationException, IOException, SAXException {

		List<String> ownerList = new LinkedList<>();
		Set<String> tmpList = new HashSet<>();

		DocumentBuilder builder = null;
		Document document = null;

		builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		document = builder.parse(inputStream);

		NodeList srNode = document.getElementsByTagName("PERSONGROUPTEAM");
		for (int i = 0; i < srNode.getLength(); i++) {
			Element element =(Element) srNode.item(i);
			String owner = getNodeValue(element,"RESPPARTY");
			tmpList.add(owner);
		}
		ownerList.addAll(tmpList);
		return ownerList;
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
		ticket.setOwner(getNodeValue(element, "OWNER"));
		ticket.setReportDate(getNodeValue(element,"REPORTDATE"));
		ticket.setReportedBy(getNodeValue(element, "REPORTEDBY"));
		ticket.setStatus(getNodeValue(element, "STATUS"));
		ticket.setStatusDate(getNodeValue(element, "STATUSDATE"));
		ticket.setTicketId(getNodeValue(element, "TICKETID"));
		ticket.setClassStructure(getNodeValue(element, "CLASSSTRUCTUREID"));
		ticket.setPriority(getNodeValue(element, "INTERNALPRIORITY"));

        NodeList wlNode = element.getElementsByTagName("WORKLOG");
        List<WorkLog> workLogs = new ArrayList<>();
        for (int i = 0; i < wlNode.getLength(); i++) {
            WorkLog workLog = transformWorkLog((Element) wlNode.item(i));
            workLogs.add(workLog);
        }
        ticket.setWorkLogs(workLogs);
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

	private static String getNodeValue(Element element, String tag) {
		return element.getElementsByTagName(tag).item(0).getTextContent();
	}
}
