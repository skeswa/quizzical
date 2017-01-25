package org.gauntlet.core.commons.util;

import java.lang.reflect.Method;


public class InitialThreadLocal<T> extends CentralizedThreadLocal<T> {

	public InitialThreadLocal(String name, T initialValue)  {
		this(name, initialValue, false);
	}

	public InitialThreadLocal(String name, T initialValue, boolean shortLived)  {
		super(shortLived);

		_name = name;
		_initialValue = initialValue;

		if (_initialValue instanceof Cloneable) {
			try {
				_cloneMethod = _initialValue.getClass().getMethod(
					_METHOD_CLONE);
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public String toString() {
		if (_name != null) {
			return _name;
		}
		else {
			return super.toString();
		}
	}

	@Override
	protected T initialValue() {
		if (_cloneMethod != null) {
			try {
				return (T)_cloneMethod.invoke(_initialValue);
			}
			catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		return _initialValue;
	}

	private static final String _METHOD_CLONE = "clone";

	private Method _cloneMethod;
	private T _initialValue;
	private String _name;

}