package com.gmmapowell.swimlane.eclipse.interfaces;

public interface ViewLayout {
	void addHexagon(int pos, HexData hi);
	void addHexagonPort(int pos, PortLocation loc, PortData port);
	void addAdapter(int hex, PortLocation ploc, int aloc, AdapterData adapter);
}
