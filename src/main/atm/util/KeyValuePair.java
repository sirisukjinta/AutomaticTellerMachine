// Copyright 2001-2014: Thomson Reuters Global Resources: All Rights Reserved. Proprietary and confidential information
// of TRGR. Disclosure, Use or Reproduction without the written authorization of TRGR is prohibited.
package main.atm.util;

public class KeyValuePair<K, V> {

	private K key;
	private V value;

	public KeyValuePair() {
	}

	public KeyValuePair(K key, V value) {
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		return this.key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return this.value;
	}

	public void setValue(V value) {
		this.value = value;
	}

}
