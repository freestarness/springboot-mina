package cn.mzh.demo.socket;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 * 自定义解码器(暂不用)
 * @author zhj-hu
 *
 */
public class SocketEncoder extends ProtocolEncoderAdapter {
	// 编码 将数据包转成字节数组
	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		// //根据报文长度开辟空间
		// IoBuffer buff = IoBuffer.allocate(1024);
		// //设置为可自动扩展空间
		// buff.setAutoExpand(true);
		// buff.putString(val, encoder)
		// buff.put((IoBuffer)message);
		//
		// //将报文中的信息添加到buff中
		// buff.flip();
		//
		// out.write(message);
		// out.flush();

	}
}