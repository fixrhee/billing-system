package org.billing.api.processor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.mule.api.MuleEventContext;
import org.mule.api.lifecycle.Callable;
import org.mule.module.client.MuleClient;

import com.opencsv.CSVReader;

public class BulkMemberProcessor implements Callable {

	@Override
	public Object onCall(MuleEventContext eventContext) throws Exception {
		InputStream fileStream = new ByteArrayInputStream((byte[]) eventContext.getMessage().getPayload());
		String filename = eventContext.getMessage().getInboundProperty("originalFilename");
		String billerID = filename.split("_")[0];
		MuleClient muleClient = new MuleClient(eventContext.getMuleContext());
		try (InputStreamReader isr = new InputStreamReader(fileStream, StandardCharsets.UTF_8);
				CSVReader reader = new CSVReader(isr)) {
			String[] nextLine;
			while ((nextLine = reader.readNext()) != null) {
				Map<String, String> mMap = new HashMap<String, String>();
				mMap.put("billerID", billerID);
				for (int i = 0; i < nextLine.length; i++) {
					if (i == 0) {
						mMap.put("name", nextLine[i]);
					}
					if (i == 1) {
						mMap.put("msisdn", nextLine[i]);
					}
					if (i == 2) {
						mMap.put("email", nextLine[i]);
					}
					if (i == 3) {
						mMap.put("address", nextLine[i]);
					}
					if (i == 4) {
						mMap.put("idCard", nextLine[i]);
					}
				}
				muleClient.dispatch("vm://bulkMember", mMap, null);
			}
			return null;
		}
	}

}
