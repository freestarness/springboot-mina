package cn.mzh.demo.socket;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.textline.TextLineEncoder;

/**
 * 自定义解码器(暂不用)
 * @author John Doe
 * @since 2018/05/02 11:35:00
 */
public class SocketEncoder extends ProtocolEncoderAdapter {
	
	private static final AttributeKey ENCODER = new AttributeKey(SocketEncoder.class, "encoder");
	//输出文本最大长度
    private int maxLineLength = Integer.MAX_VALUE;
	//编码
	private final Charset charset;
	
	public SocketEncoder(Charset charset) {
		super();
		this.charset = charset;
	}
	// 编码 将数据包转成字节数组
	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		
		CharsetEncoder encoder = (CharsetEncoder) session.getAttribute(ENCODER);
        if (encoder == null) {
            encoder = charset.newEncoder();
            session.setAttribute(ENCODER, encoder);
        }
        String value = (message == null ? "" : message.toString());
        IoBuffer buf = IoBuffer.allocate(value.length()).setAutoExpand(true);
        buf.putString(value, encoder);

        if (buf.position() > maxLineLength) {
            throw new IllegalArgumentException("Line length: " + buf.position());
        }
        buf.flip();
        out.write(buf);
	    out.flush();
	}
}