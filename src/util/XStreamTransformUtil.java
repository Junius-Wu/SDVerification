package util;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DoubleConverter;
import com.thoughtworks.xstream.converters.basic.FloatConverter;
import com.thoughtworks.xstream.converters.basic.IntConverter;
import com.thoughtworks.xstream.converters.basic.LongConverter;
import com.thoughtworks.xstream.io.xml.DomDriver;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class XStreamTransformUtil {

    private final static String TAG = XStreamTransformUtil.class.getSimpleName();
   
    @SuppressWarnings("unchecked")
    public static <T> T toBean(Class<T> type, InputStream is) { 
//       json用 JettisonMappedXmlDriver()  || xml用 DomDriver()
        XStream xmStream = new XStream(new DomDriver("UTF-8"));
        // 设置可忽略为在javabean类中定义的界面属性
        xmStream.ignoreUnknownElements();
        xmStream.registerConverter(new MyIntCoverter());
        xmStream.registerConverter(new MyLongCoverter());
        xmStream.registerConverter(new MyFloatCoverter());
        xmStream.registerConverter(new MyDoubleCoverter());
        xmStream.processAnnotations(type);
        T obj = null;
        try {
            obj = (T) xmStream.fromXML(is);
        } catch (Exception e) {
            System.out.println("转换异常");
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                  
                }
            }
        }
        return obj;
    }
    
    
    public static <T> T toBean(Class<T> type, byte[] bytes) { //直接传入byte
        if (bytes == null) return null;
        return toBean(type, new ByteArrayInputStream(bytes));
    }

    private static class MyIntCoverter extends IntConverter {

        @Override
        public Object fromString(String str) {
            int value;
            try {
                value = (Integer) super.fromString(str);
            } catch (Exception e) {
                value = 0;
            }
            return value;
        }

        @Override
        public String toString(Object obj) {
            return super.toString(obj);
        }
    }

    private static class MyLongCoverter extends LongConverter {
        @Override
        public Object fromString(String str) {
            long value;
            try {
                value = (Long) super.fromString(str);
            } catch (Exception e) {
                value = 0;
            }
            return value;
        }

        @Override
        public String toString(Object obj) {
            return super.toString(obj);
        }
    }

    private static class MyFloatCoverter extends FloatConverter {
        @Override
        public Object fromString(String str) {
            float value;
            try {
                value = (Float) super.fromString(str);
            } catch (Exception e) {
                value = 0;
            }
            return value;
        }

        @Override
        public String toString(Object obj) {
            return super.toString(obj);
        }
    }

    private static class MyDoubleCoverter extends DoubleConverter {
        @Override
        public Object fromString(String str) {
            double value;
            try {
                value = (Double) super.fromString(str);
            } catch (Exception e) {
                value = 0;
            }
            return value;
        }

        @Override
        public String toString(Object obj) {
            return super.toString(obj);
        }
    }
}
