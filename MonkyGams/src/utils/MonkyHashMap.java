package utils;

import java.util.HashMap;

@SuppressWarnings("serial")
public class MonkyHashMap<K> extends HashMap<K, Integer>{

	public void increment(K key) {
		
		if (containsKey(key)) {
			put(key, get(key) + 1);
		}
		else {
			put(key, 1);
		}
		
	}
	
	public int getNonThrowing(K key) {
		if (containsKey(key)) {
			return 0;
		}
		else {
			return get(key);
		}
	}
	
}
