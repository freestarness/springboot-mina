package cn.mzh.demo.socket;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

/**
 * 自定义解码器
 * @author John Doe
 * @since 2018/04/28 12:14:18
 */
public class SocketDecoder extends CumulativeProtocolDecoder {
	/**编码集合*/
	private final Charset charset;

	public SocketDecoder(Charset charset) {
		super();
		this.charset = charset;
	}
	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {

		if (in.remaining() > 0) {// 有数据时，读取8字节判断消息长度
			byte[] sizeBytes = new byte[8];
			in.mark();// 标记当前位置，以便reset
			in.get(sizeBytes);// 读取前8字节
			in.reset();
			int size = Integer.valueOf(String.valueOf(charset.decode(ByteBuffer.wrap(sizeBytes))));
			// 如果消息内容的长度不够则直接返回true
			if (size  > in.remaining()) {// 如果消息内容不够，则重置，相当于不读取size
				in.reset();
				return false;// 接收新数据，以拼凑成完整数据
			} else {
				byte[] data = new byte[in.remaining()];
				in.get(data,0,size);
				String str = new String(data,charset);
				out.write(str);
				//直接读取完毕不考虑粘包问题？
				if (in.remaining() > 0) {
					return true;
				}
			}
		}
		return false;
	}
}
