package com.jianingsun.tlv.myTLV;

import java.util.List;
import java.util.ArrayList;

/**
 * Created by jianingsun on 2018-02-23.
 */

public class BerTlvBox {

    protected BerTlvBox(List<BerTlv> aTlvs) {
        tlvs = aTlvs;
    }

    private final List<BerTlv> tlvs;

    public BerTlv find(BerTag aTag) {
        for (BerTlv tlv : tlvs) {
            BerTlv found = tlv.find(aTag);
            if(found!=null) {
                return found;
            }
        }
        return null;
    }

    public List<BerTlv> findAll(BerTag aTag) {
        List<BerTlv> list = new ArrayList<BerTlv>();
        for (BerTlv tlv : tlvs) {
            list.addAll(tlv.findAll(aTag));
        }
        return list;
    }

    public List<BerTlv> getList() {
        return tlvs;
    }

    @Override
    public String toString() {
        return "BerTlvBox{" +
                "tlvs=" + tlvs +
                '}';
    }

}
