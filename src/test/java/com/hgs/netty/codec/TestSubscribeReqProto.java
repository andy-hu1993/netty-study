package com.hgs.netty.codec;

import java.util.ArrayList;
import java.util.List;

import com.google.protobuf.InvalidProtocolBufferException;

/**
 * test protobuf <br>
 *
 * @author huguangsheng
 * @version V1.0
 * @date 2018/8/31 下午5:01
 */
public class TestSubscribeReqProto {

    private static byte[] encode(SubscribeReqProto.SubscribeReq req){
        return req.toByteArray();
    }

    private static SubscribeReqProto.SubscribeReq decode(byte[] body) throws InvalidProtocolBufferException {
        return SubscribeReqProto.SubscribeReq.parseFrom(body);
    }

    private static SubscribeReqProto.SubscribeReq createSubscribeReq(){
        SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
        builder.setSubReqID(1);
        builder.setProductName("protobuf");
        builder.setUserName("huguangsheng");
        List<String> addressList = new ArrayList<String>();
        addressList.add("beijing");
        addressList.add("hubei");
        addressList.add("hangzhou");
        builder.addAllAddress(addressList);
        return builder.build();
    }

    public static void main(String[] args) throws InvalidProtocolBufferException {
        SubscribeReqProto.SubscribeReq req = createSubscribeReq();
        System.out.println("before " + req.toString());

        SubscribeReqProto.SubscribeReq req2 = decode(encode(req));
        System.out.println("after" + req2.toString());

        System.out.println(req.equals(req2));
    }

}
