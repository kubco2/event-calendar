package cz.muni.fi.pv.projekt;

import javax.annotation.Resource;
import javax.sql.DataSource;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author zkrejcov
 */
@Repository("dbh")
public class DBHelper {
    private JdbcTemplate jdbc;
    private DataSource ds;
    private ApplicationContext springCtx;
    
    @Autowired
    public DBHelper(ApplicationContext springCtx) {
        this.springCtx = springCtx;
    }
    
    @Resource
    public void setDataSource(DataSource dataSource) {
        ds = dataSource;
        jdbc = new JdbcTemplate(ds);
    }
    
    public JdbcTemplate getJdbc() {
        return jdbc;
    }
    
    public DataSource getDS() {
        return ds;
    }
    
    public ApplicationContext getSpringContext() {
        return springCtx;
    }
}
