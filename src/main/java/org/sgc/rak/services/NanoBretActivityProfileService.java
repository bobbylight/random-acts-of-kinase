package org.sgc.rak.services;

import org.sgc.rak.dao.NanoBretActivityProfileDao;
import org.sgc.rak.i18n.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service for manipulating NanoBRET activity profiles.
 */
@Service
public class NanoBretActivityProfileService {

    private final NanoBretActivityProfileDao activityProfileDao;
    private final CompoundService compoundService;
    private final KinaseService kinaseService;

    private final Messages messages;

    private static final Logger LOGGER = LoggerFactory.getLogger(NanoBretActivityProfileService.class);

    @Autowired
    public NanoBretActivityProfileService(NanoBretActivityProfileDao activityProfileDao,
                                          CompoundService compoundService,
                                          KinaseService kinaseService, Messages messages) {
        this.activityProfileDao = activityProfileDao;
        this.compoundService = compoundService;
        this.kinaseService = kinaseService;
        this.messages = messages;
    }
}
