package com.uav.base.util;

import java.io.IOException;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

public class JsonResponseHandler implements ResponseHandler<JSONObject> {
	public JSONObject handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
		int status=response.getStatusLine().getStatusCode();
		if(status>=200 && status<300){
			HttpEntity entity=response.getEntity();
			if(entity!=null){
				String result=EntityUtils.toString(entity);
				return JSONObject.fromObject(result);
			}else{
				return null;
			}
		}else{
			throw new ClientProtocolException("Unexpected response status:"+status);
		}
	}
}