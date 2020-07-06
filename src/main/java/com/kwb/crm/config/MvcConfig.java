package com.kwb.crm.config;

import com.kwb.crm.interceptors.NoLoginInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class MvcConfig  extends WebMvcConfigurerAdapter {

    /**
     * 注入一个拦截器
     * @return
     */
        @Bean
        public NoLoginInterceptor noLoginInterceptor (){
            return new NoLoginInterceptor();
        }

    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //需要一个实现了拦截器
        registry.addInterceptor(noLoginInterceptor())
                //用于设置拦截器的过滤路径规则（拦截所有）
                .addPathPatterns("/**")
                //放行的资源
                .excludePathPatterns("/index"
                                    ,"/user/login"
                                    , "/css/**"
                                    , "/images/**"
                                    , "/js/**"
                                    ,"/lib/**");
    }
}
