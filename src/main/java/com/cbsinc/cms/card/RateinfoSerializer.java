package com.cbsinc.cms.card;

import org.w3c.dom.*;
import org.apache.soap.rpc.*;
import org.apache.soap.util.*;
import org.apache.soap.util.xml.*;

public class RateinfoSerializer implements Deserializer {
	public Bean unmarshall(String inScopeEncStyle, QName elementType, Node src, XMLJavaMappingRegistry xjmr,
			SOAPContext ctx) throws IllegalArgumentException {
		Element svcElement = (Element) src;
		Element tempEl = DOMUtils.getFirstChildElement(svcElement);
		RateInfo svc = new RateInfo();

		while (tempEl != null) {
			String tagName = tempEl.getTagName();

			if (tagName.equals("currency")) {
				Bean bean = xjmr.unmarshall(inScopeEncStyle, RPCConstants.Q_ELEM_PARAMETER, tempEl, ctx);
				Parameter param = (Parameter) bean.value;

				svc.setcurrency((String) param.getValue());
			} else if (tagName.equals("date")) {
				Bean bean = xjmr.unmarshall(inScopeEncStyle, RPCConstants.Q_ELEM_PARAMETER, tempEl, ctx);
				Parameter param = (Parameter) bean.value;

				svc.setdate((String) param.getValue());
			} else if (tagName.equals("rate")) {
				Bean bean = xjmr.unmarshall(inScopeEncStyle, RPCConstants.Q_ELEM_PARAMETER, tempEl, ctx);
				Parameter param = (Parameter) bean.value;

				svc.setrate((String) param.getValue());
			}

			tempEl = DOMUtils.getNextSiblingElement(tempEl);
		}

		return new Bean(CardInfo.class, svc);
	}
}
