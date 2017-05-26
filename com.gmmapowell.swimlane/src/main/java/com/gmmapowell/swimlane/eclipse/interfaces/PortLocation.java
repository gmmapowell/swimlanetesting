package com.gmmapowell.swimlane.eclipse.interfaces;

public enum PortLocation {
	NORTHEAST {
		@Override
		public String toString() {
			return "ne";
		}
	},
	NORTHWEST {
		@Override
		public String toString() {
			return "nw";
		}
	},
	SOUTHWEST {
		@Override
		public String toString() {
			return "sw";
		}
	},
	SOUTHEAST {
		@Override
		public String toString() {
			return "se";
		}
	}
}
