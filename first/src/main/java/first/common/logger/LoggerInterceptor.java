package first.common.logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoggerInterceptor extends HandlerInterceptorAdapter{
	
//	protected Log log = LogFactory.getLog(LoggerInterceptor.class);
	
	private final Logger log= LoggerFactory.getLogger(this.getClass());
	
	//컨트롤러가 호출되기 전에 실행
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handle) throws Exception {
		if(log.isDebugEnabled()) {
			log.debug("==============START============");
			log.debug("request URL \t: " +request.getRequestURL()); //현재 호출된 URI가 무엇인지 보여준다.
		}
		return super.preHandle(request, response, handle);
	}
	
	//컨트롤러가 실행되고 난 후 호출
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView modelAndView) throws Exception{
		if(log.isDebugEnabled()) {
			log.debug("============end===========");
		}
	}
}