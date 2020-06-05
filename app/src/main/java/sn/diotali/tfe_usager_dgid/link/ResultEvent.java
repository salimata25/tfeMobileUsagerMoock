/*
 * ============================================================================
 * COPYRIGHT
 *              Pax CORPORATION PROPRIETARY INFORMATION
 *   This software is supplied under the terms of a license agreement or
 *   nondisclosure agreement with Pax Corporation and may not be copied
 *   or disclosed except in accordance with the terms in that agreement.
 *      Copyright (C) 2017 - ? Pax Corporation. All rights reserved.
 * Module Date: 2017-7-5
 * Module Author: lixc
 * Description:
 *
 * ============================================================================
 */
package sn.diotali.tfe_usager_dgid.link;

public class ResultEvent extends Event {
    public enum Status {
        SUCCESS,
        FAILED,
        DISCOVER_ONE,
        SEARCH_COMPLETE,
        CONNECT_SUCCESS,
        CONNECT_FAILED,
        DOWNLOAD_SUCCESS,
        DOWNLOAD_FAILED,
        RECV_REPORT,
        DEVIC8NOT_FOUND
    }

    public ResultEvent(Status status) {
        super(status);
    }

    public ResultEvent(Status status, Object data) {
        super(status, data);
    }
}
