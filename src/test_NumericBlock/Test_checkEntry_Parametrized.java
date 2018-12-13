package test_NumericBlock;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import javax.swing.ImageIcon;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.swisshof.selfcheckout.gui.NumericBlock;
import com.swisshof.selfcheckout.gui.NumericBlockButton;
import com.swisshof.selfcheckout.gui.NumericBlock.IAmoutChanged;
import com.swisshof.selfcheckout.gui.NumericBlockButton.Digit;

import com.swisshof.selfcheckout.ResourceProvider;
import com.swisshof.selfcheckout.SelfCheckoutContext;


class NumericBlockAccess extends NumericBlock
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NumericBlockAccess(SelfCheckoutContext context, IAmoutChanged amountChangedListener) {
		super(context, amountChangedListener);
	}

	public void enterDigit(Digit digit)
	{
		queueAmountEntry.add(digit);
	}

	public ArrayList<NumericBlockButton.Digit> getQueue()
	{
		return queueAmountEntry;
	}
	
	@Override
	public void processDigit(Digit digit) {
		super.processDigit(digit);
	}

	@Override
	public ArrayList<Digit> checkEntry() {
		return super.checkEntry();
	}

	
}

class Test_processDigit {
	
	SelfCheckoutContext context = null;
	IAmoutChanged amountChangedListener = null;

	public void before()
	{
		context = new SelfCheckoutContext();
		context.setResourceProvider(new ResourceProvider());
		
		amountChangedListener = new IAmoutChanged() {

			@Override
			public void amountEntryChanged(double newAmount) {
			}
				
		};
	}
	
    @SuppressWarnings("unused")
	private static Stream<Arguments> amounts() {
        return Stream.of(
        		Arguments.of(new Digit[]{Digit.ONE}, 1.0),
        		Arguments.of(new Digit[]{Digit.TWO}, 2.0),
        		Arguments.of(new Digit[]{Digit.THREE}, 3.0),
        		Arguments.of(new Digit[]{Digit.FOUR}, 4.0),
        		Arguments.of(new Digit[]{Digit.FIVE}, 5.0),
        		Arguments.of(new Digit[]{Digit.SIX}, 6.0),
        		Arguments.of(new Digit[]{Digit.SEVEN}, 7.0),
        		Arguments.of(new Digit[]{Digit.EIGHT}, 8.0),
        		Arguments.of(new Digit[]{Digit.NINE}, 9.0),
           		Arguments.of(new Digit[]{Digit.ONE, Digit.ZERO}, 10.0),
      		
        		Arguments.of(new Digit[]{Digit.ONE, Digit.TWO}, 12.00),
                Arguments.of(new Digit[]{Digit.THREE, Digit.FOUR}, 34.00),
                Arguments.of(new Digit[]{Digit.THREE, Digit.DOT, Digit.FOUR}, 3.40),
                
                Arguments.of(new Digit[]{Digit.ZERO, Digit.ONE, Digit.DOT, Digit.FOUR}, 1.40)

        );
    }

	@ParameterizedTest
	@MethodSource("amounts")
	void testGetAmount(Digit[] digits, double expectedAmount) {
		before();
		
		NumericBlockAccess numericBlock = new NumericBlockAccess(context, amountChangedListener);
		for (Digit d : digits) {
			numericBlock.processDigit(d);
		}

		assertEquals(expectedAmount, numericBlock.getAmount());
	}
	
	
    @SuppressWarnings("unused")
	private static Stream<Arguments> dots() {
        return Stream.of(
        		Arguments.of(new Digit[]{Digit.ONE, Digit.DOT,Digit.ONE}, 1.1),
        		Arguments.of(new Digit[]{Digit.NINE, Digit.EIGHT, Digit.DOT,Digit.TWO}, 98.20),
        		Arguments.of(new Digit[]{Digit.SEVEN, Digit.SIX, Digit.FIVE, Digit.DOT,Digit.THREE, Digit.FIVE}, 765.35),

           		Arguments.of(new Digit[]{Digit.DOT,Digit.FIVE}, 0.50),
           		Arguments.of(new Digit[]{Digit.DOT,Digit.SIX, Digit.FIVE}, 0.65)

        );
    }

	@ParameterizedTest
	@MethodSource("dots")
	void testGetDots(Digit[] digits, double expectedAmount) {
		before();
		
		NumericBlockAccess numericBlock = new NumericBlockAccess(context, amountChangedListener);
		for (Digit d : digits) {
			numericBlock.processDigit(d);
		}

		assertEquals(expectedAmount, numericBlock.getAmount());
	}

    @SuppressWarnings("unused")
	private static Stream<Arguments> accept_5_10_cents() {
        return Stream.of(

         		Arguments.of(new Digit[]{Digit.ONE, Digit.DOT, Digit.ONE, Digit.FIVE}, 1.15),
         		Arguments.of(new Digit[]{Digit.ONE, Digit.DOT, Digit.ONE, Digit.ZERO}, 1.1),
        		
          		Arguments.of(new Digit[]{Digit.ONE, Digit.DOT, Digit.ONE, Digit.ONE}, 1.10),
          		Arguments.of(new Digit[]{Digit.ONE, Digit.DOT, Digit.ONE, Digit.TWO}, 1.10),
          		Arguments.of(new Digit[]{Digit.ONE, Digit.DOT, Digit.ONE, Digit.THREE}, 1.10),
          		Arguments.of(new Digit[]{Digit.ONE, Digit.DOT, Digit.ONE, Digit.FOUR}, 1.10),
          		Arguments.of(new Digit[]{Digit.ONE, Digit.DOT, Digit.ONE, Digit.SIX}, 1.10),
          		Arguments.of(new Digit[]{Digit.ONE, Digit.DOT, Digit.ONE, Digit.SEVEN}, 1.10),
          		Arguments.of(new Digit[]{Digit.ONE, Digit.DOT, Digit.ONE, Digit.EIGHT}, 1.10),
          		Arguments.of(new Digit[]{Digit.ONE, Digit.DOT, Digit.ONE, Digit.NINE}, 1.10)

        );
    }

	@ParameterizedTest
	@MethodSource("accept_5_10_cents")
	void testAcceptOnly_5_10_Cents(Digit[] digits, double expectedAmount) {
		before();
		
		NumericBlockAccess numericBlock = new NumericBlockAccess(context, amountChangedListener);
		for (Digit d : digits) {
			numericBlock.processDigit(d);
		}

		assertEquals(expectedAmount, numericBlock.getAmount());	
	}
	
	
    @SuppressWarnings("unused")
	private static Stream<Arguments> enterAndClearDigits() {
        return Stream.of(
        		// 7 2 . < 3 . 8
        		Arguments.of(new Digit[]{Digit.SEVEN,  Digit.TWO, Digit.DOT, Digit.CLEAR, Digit.THREE, Digit.DOT, Digit.EIGHT},
   				     		new Digit[]{Digit.SEVEN, Digit.TWO, Digit.THREE, Digit.DOT, Digit.EIGHT}, 723.8),
        		// 1 2 1 . 2 < . 3
        		Arguments.of(new Digit[]{Digit.ONE,  Digit.TWO, Digit.ONE, Digit.DOT, Digit.CLEAR, Digit.DOT, Digit.THREE},
   				     		new Digit[]{Digit.ONE, Digit.TWO, Digit.ONE, Digit.DOT, Digit.THREE}, 121.3),
        		// <
        		Arguments.of(new Digit[]{Digit.CLEAR},
   				     		new Digit[]{}, 0.0)
        		
        );
    }

	@ParameterizedTest
	@MethodSource("enterAndClearDigits")
	void test_EnterAndClearDigits(Digit[] enteredDigits, Digit[] expectedDigits, double expectedAmount) {
		before();
		
		NumericBlockAccess numericBlock = new NumericBlockAccess(context, amountChangedListener);
		for (Digit d : enteredDigits) {
			numericBlock.processDigit(d);
		}

		List<Digit> cleanupDigits = numericBlock.getQueue();
		
		assertArrayEquals(expectedDigits, cleanupDigits.toArray(new Digit[0]));
		assertEquals(expectedAmount, numericBlock.getAmount());
	}
}


class Test_checkEntry {
	
	SelfCheckoutContext context = null;
	IAmoutChanged amountChangedListener = null;

	public void before()
	{
		context = new SelfCheckoutContext();
		context.setResourceProvider(new ResourceProvider());
		
		amountChangedListener = new IAmoutChanged() {

			@Override
			public void amountEntryChanged(double newAmount) {
			}
				
		};
	}
	
    @SuppressWarnings("unused")
	private static Stream<Arguments> clearDigits() {
        return Stream.of(
        		// 1
        		Arguments.of(new Digit[]{Digit.ONE, }, 
        				     new Digit[] {Digit.ONE, }),
        		// 1 2
        		Arguments.of(new Digit[]{Digit.ONE, Digit.TWO},
        				     new Digit[]{Digit.ONE, Digit.TWO}),

        		// 1 < 2
        		Arguments.of(new Digit[]{Digit.ONE, Digit.CLEAR, Digit.TWO},
   				     		new Digit[]{Digit.TWO}),

        		// 1 . <
        		Arguments.of(new Digit[]{Digit.ONE, Digit.DOT, Digit.CLEAR},
   				     		new Digit[]{Digit.ONE}),

        		// 5 . 2 5 <
        		Arguments.of(new Digit[]{Digit.FIVE, Digit.DOT, Digit.TWO, Digit.FIVE, Digit.CLEAR},
   				     		new Digit[]{Digit.FIVE, Digit.DOT, Digit.TWO}),
        		
        		// 7 2 . < 3
        		Arguments.of(new Digit[]{Digit.SEVEN,  Digit.TWO, Digit.DOT, Digit.CLEAR, Digit.THREE},
   				     		new Digit[]{Digit.SEVEN, Digit.TWO, Digit.THREE})
    		
        );
    }

	@ParameterizedTest
	@MethodSource("clearDigits")
	void test_ClearDigits(Digit[] enteredDigits, Digit[] expectedDigits) {
		before();
		
		NumericBlockAccess numericBlock = new NumericBlockAccess(context, amountChangedListener);
		for (Digit d : enteredDigits) {
			numericBlock.enterDigit(d);
		}

		List<Digit> cleanupDigits =numericBlock.checkEntry();
		
		assertArrayEquals(expectedDigits, cleanupDigits.toArray(new Digit[0]));
	}

}
