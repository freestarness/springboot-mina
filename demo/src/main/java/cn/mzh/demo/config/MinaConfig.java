package cn.mzh.demo.config;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.mzh.demo.socket.SocketFactory;
import cn.mzh.demo.socket.handler.ServerHandler;

import static cn.mzh.demo.App.logger;

/**
 * mina配置相关信息
 * @author John Doe
 * @since 2018/04/28 12:14:18
 */
@Configuration
public class MinaConfig {
	// socket占用端口
	@Value("${mina.port}")
	private int port;

	@Bean
	public LoggingFilter loggingFilter() {
		return new LoggingFilter();
	}

	@Bean
	public IoHandler ioHandler() {
		return new ServerHandler();
	}

	@Bean
	public InetSocketAddress inetSocketAddress() {
		return new InetSocketAddress(port);
	}

	@Bean
	public IoAcceptor ioAcceptor() throws Exception {
		IoAcceptor acceptor = new NioSocketAcceptor();
		acceptor.getFilterChain().addLast("logger", loggingFilter());

		// 使用自定义编码解码工厂类
		acceptor.getFilterChain().addLast("coderc", new ProtocolCodecFilter(new SocketFactory(Charset.forName("GBK"))));
		acceptor.setHandler(ioHandler());

		acceptor.getSessionConfig().setReadBufferSize(2048);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);

		acceptor.bind(inetSocketAddress());
		logger.info("Socket服务器在端口：" + port + "已经启动");
		return acceptor;
	}

}