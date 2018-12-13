package test_Statemachine;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.swisshof.selfcheckout.ResourceProvider;
import com.swisshof.selfcheckout.SelfCheckoutContext;
import com.swisshof.selfcheckout.gui.IGui;
import com.swisshof.selfcheckout.statemachine.MainStm;
import com.swisshof.selfcheckout.statemachine.MainStm.Events;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class test_statemachine {
	
	protected SelfCheckoutContext context = null;
	@Mock 
	IGui gui;

	@BeforeEach
	public void before()
	{
		context = new SelfCheckoutContext();
		context.setResourceProvider(new ResourceProvider());

	}

	@Test
	public void test_idle()
	{


		MainStm mainStm = new MainStm(context);
		mainStm.init();
		mainStm.processEvent(Events.AMOUNT_CHANGED);
	}
}
