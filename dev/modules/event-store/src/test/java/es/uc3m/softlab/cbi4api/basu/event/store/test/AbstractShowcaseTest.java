/* 
 * $Id: AbstractShowcaseTest.java,v 1.0 2013-01-27 23:29:05 averab Exp $
 *
 * @copyright Universidad Carlos III de Madrid. proprietary/confidential. Use is subject to license terms.
 */
package es.uc3m.softlab.cbi4api.gbas.event.store.test;

import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.BeforeTransaction;

/**
 * @author averab
 */
public abstract class AbstractShowcaseTest extends AbstractTransactionalJUnit4SpringContextTests {

    @BeforeTransaction
    public void setupData() throws Exception {
    	/*
         * if (countRowsInTable("Customer") == 0) {
         *    executeSqlScript("classpath:data.sql", false);
         * }
         */
    }
}