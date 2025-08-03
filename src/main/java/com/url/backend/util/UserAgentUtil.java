package com.url.backend.util;

import com.url.backend.DTO.ClientDto;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;

public class UserAgentUtil {

    private static final UserAgentAnalyzer uaa = UserAgentAnalyzer
            .newBuilder()
            .hideMatcherLoadStats()
            .withField("AgentName")
            .withField("AgentVersion")
            .withField("OperatingSystemName")
            .withField("DeviceName")
            .build();

    public static ClientDto extractInfo(String userAgentString, String ip, String referrer) {
        UserAgent agent = uaa.parse(userAgentString);

        ClientDto dto = new ClientDto();
        dto.setBrowser(agent.getValue("AgentName"));
        dto.setBrowserVersion(agent.getValue("AgentVersion"));
        dto.setOs(agent.getValue("OperatingSystemName"));
        dto.setDevice(agent.getValue("DeviceName"));
        dto.setIp(ip);
        dto.setReferrer(referrer != null ? referrer : "direct");
        dto.setUserAgent(userAgentString);

        return dto;
    }
}
 