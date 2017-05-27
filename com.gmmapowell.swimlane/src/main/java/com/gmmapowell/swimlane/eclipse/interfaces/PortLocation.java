package com.gmmapowell.swimlane.eclipse.interfaces;

public enum PortLocation {
	NORTHEAST {
		@Override
		public String toString() {
			return "ne";
		}

		@Override
		public int x(int x) {
			return x;
		}

		@Override
		public int y(int y) {
			return -y;
		}
	},
	NORTHWEST {
		@Override
		public String toString() {
			return "nw";
		}

		@Override
		public int x(int x) {
			return -x;
		}

		@Override
		public int y(int y) {
			return -y;
		}
	},
	SOUTHWEST {
		@Override
		public String toString() {
			return "sw";
		}

		@Override
		public int x(int x) {
			return -x;
		}

		@Override
		public int y(int y) {
			return y;
		}
	},
	SOUTHEAST {
		@Override
		public String toString() {
			return "se";
		}

		@Override
		public int x(int x) {
			return x;
		}

		@Override
		public int y(int y) {
			return y;
		}
	};

	public abstract int x(int x);
	public abstract int y(int y);
}
