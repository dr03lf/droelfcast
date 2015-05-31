package at.droelf.droelfcast.common;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

public class Hash {

    public static String md5(String string){
//            return DigestUtils.md5Hex(string.getBytes("UTF-8"));

          return new String(Hex.encodeHex(DigestUtils.md5(string)));
    }

}
