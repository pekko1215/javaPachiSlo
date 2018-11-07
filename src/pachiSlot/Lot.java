package pachiSlot;

public class Lot {
	public String name = null;
	public int getProbability(Slot slot) {
		return 0;
	}
	public ControlCode getControlCode(Slot slot) {
		if(slot.bonusFlag != null) {
			return slot.bonusFlag.getControlCode(slot);
		}
		return ControlCode.はずれ;
	}
}
