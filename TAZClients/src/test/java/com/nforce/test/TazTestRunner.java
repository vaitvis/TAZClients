package com.nforce.test;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import com.airhacks.afterburner.injection.Injector;

public class TazTestRunner extends BlockJUnit4ClassRunner {

	public TazTestRunner(Class<?> klass) throws InitializationError {
		super(klass);
	}

	protected Object createTest() throws Exception {
		return Injector.instantiatePresenter(getTestClass().getJavaClass());
	}
	
	@Override
	public void run(RunNotifier notifier) {
		super.run(notifier);
		Injector.forgetAll();
	}
}
