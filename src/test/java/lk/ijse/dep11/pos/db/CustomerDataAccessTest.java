package lk.ijse.dep11.pos.db;

import lk.ijse.dep11.pos.tm.Customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CustomerDataAccessTest {
    @Test
    void sqlSyntax(){
        assertDoesNotThrow(() -> Class.forName("lk.ijse.dep11.pos.db.CustomerDataAccess"));
    }
    @BeforeEach
    void setUp() throws SQLException {
        SingleConnectionDataSource.getInstance().getConnection().setAutoCommit(false);
    }
    @AfterEach
    void tearDown() throws SQLException {
        SingleConnectionDataSource.getInstance().getConnection().rollback();
        SingleConnectionDataSource.getInstance().getConnection().setAutoCommit(true);
    }

    @Test
    void getAllCustomers() throws SQLException {
        CustomerDataAccess.saveCustomer(new Customer("0001", "Nuwan", "Panadura"));
        CustomerDataAccess.saveCustomer(new Customer("0002", "Saman", "Galle"));
        assertDoesNotThrow(()->{
            List<Customer> customerList = CustomerDataAccess.getAllCustomers();
            assertTrue(customerList.size() >= 2);
        });
    }

    @Test
    void saveCustomer() {
        assertDoesNotThrow(()->{
            CustomerDataAccess.saveCustomer(new Customer("0001", "Nuwan", "Panadura"));
            CustomerDataAccess.saveCustomer(new Customer("0002", "Saman", "Galle"));
        });
        assertThrows(SQLException.class, ()-> CustomerDataAccess
                .saveCustomer(new Customer("0001", "Kasun", "Galle")));
    }

    @Test
    void updateCustomer() throws SQLException {
        CustomerDataAccess.saveCustomer(new Customer("0001", "Nuwan", "Panadura"));
        assertDoesNotThrow(()-> CustomerDataAccess
                .updateCustomer(new Customer("0001", "Ruwan", "Matara")));
    }

    @Test
    void deleteCustomer() throws SQLException {
        CustomerDataAccess.saveCustomer(new Customer("0001", "Nuwan", "Panadura"));
        int size = CustomerDataAccess.getAllCustomers().size();
        assertDoesNotThrow(() ->{
            CustomerDataAccess.deleteCustomer("0001");
            assertEquals(size - 1, CustomerDataAccess.getAllCustomers().size());
        });

    }

    @Test
    void getLastCustomerId() throws SQLException {
        String lastCustomerId = CustomerDataAccess.getLastCustomerId();
        if (CustomerDataAccess.getAllCustomers().isEmpty()){
            assertNull(lastCustomerId);
        }else{
            CustomerDataAccess.saveCustomer(new Customer("0001", "Nuwan", "Panadura"));
            lastCustomerId = CustomerDataAccess.getLastCustomerId();
            assertNotNull(lastCustomerId);
        }
    }


}