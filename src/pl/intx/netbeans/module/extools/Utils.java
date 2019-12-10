/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.intx.netbeans.module.extools;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;

/**
 *
 * @author mariuszclapinski
 */
public class Utils {

    private static final Logger logger = Logger.getLogger(Utils.class.getName());

    public static class Serializer {

        public static <T> Object fromString(String s) {
            if (s.length() == 0) {
                return null;
            }

            T obj;
            try (XMLDecoder d = new XMLDecoder(new ByteArrayInputStream(s.getBytes()))) {
                obj = (T) d.readObject();
            }
            return obj;
        }

        public static String toString(Serializable o) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try (XMLEncoder encoder = new XMLEncoder(baos)) {
                encoder.writeObject(o);
            }
            return new String(baos.toByteArray());
        }
    }

    public static void showMessage(String message) {
        NotifyDescriptor.Message m = new NotifyDescriptor.Message(message);
        DialogDisplayer.getDefault().notify(m);
    }
}
