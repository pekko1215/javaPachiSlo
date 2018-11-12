package pachiSlot;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;;
public class Control {
	private int controlCount;
	private int reelLength;
	private int yakuCount;
	private int maxLine;
	public Reel reel;
	private ArrayList<Yaku> yakuList;
	private ArrayList<ArrayList<Integer>> betLine;
	private int slideTableSize;
	private ArrayList<Integer> slideTable;
	private ArrayList<Integer> tableNum1;
	private ArrayList<Integer> tableNum2;
	private ArrayList<Integer> tableNum3;
	public int chipLength;
	
	public int[] stopPos = {-1,-1,-1};
	public int stopCount = 0;
	public int[] reelStopPos = {-1,-1,-1};

	private int rcc;
	private int stopPattern;
	public Control(URL url) {
		BufferedInputStream buffer = null;
		DataInputStream stream = null;
		try {
			buffer = new BufferedInputStream(url.openStream());
			stream = new DataInputStream(buffer);

			stream.readInt();
			//リトルエンディアンのための苦肉の策
			this.controlCount = stream.readInt() >> 24;
			stream.read();
			this.reelLength = stream.read();
			this.yakuCount = stream.read();
			this.maxLine = stream.read();
			this.chipLength = 0;
			ArrayList<ArrayList<Integer>> reelArray = new ArrayList<ArrayList<Integer>>();
			for(int i = 0;i < 3;i++) {
				ArrayList<Integer> array = new ArrayList<Integer>();
				for(int j = 0;j < this.reelLength;j++) {
					int num = stream.read();
					array.add(num);
					this.chipLength = num > this.chipLength ? num : this.chipLength;
				}
				reelArray.add(array);
			}
			this.chipLength++;
			this.reel = new Reel(reelArray);
			this.yakuList = new ArrayList<Yaku>();
			this.yakuList.add(new Yaku(Role.はずれ,0x7000));
			for(int i = 0;i < this.yakuCount;i++) {
				int tmp = stream.read();
				tmp += stream.read() << 8;
				for(int j = 0;j < 3;j++) {
					if((tmp >> j * 4 & 0x0F) == 0x0F) {
						tmp += (0xF000 << j * 4);
					}
				}
				Role r = null;
				for(Role role : Role.values()) {
					if(role.ordinal() == i + 1) {
						r = role;
						break;
					}
				}
				this.yakuList.add(new Yaku(r,tmp));
			}
			this.yakuCount++;
			this.betLine = new ArrayList<ArrayList<Integer>>();
			
			for(int i = 0;i < this.maxLine;i++) {
				ArrayList<Integer> array = new ArrayList<Integer>();
				for(int j = 0;j < 4;j++) {
					array.add(stream.read());

				}
				this.betLine.add(array);
			}

			this.slideTableSize = stream.read();
			this.slideTableSize += stream.read() << 8;

			this.slideTable = new ArrayList<Integer>();
			for(int i=0;i < this.slideTableSize * this.reelLength;i++) {
				this.slideTable.add(stream.read());
			}

			this.tableNum1 = new ArrayList<Integer>();
			for(int i=0;i < this.controlCount * 3 * 2;i++) {
				this.tableNum1.add(stream.read());
			}

			this.tableNum2 = new ArrayList<Integer>();
			for(int i=0;i < this.controlCount * 6 * this.reelLength * 2;i++) {
				this.tableNum2.add(stream.read());
			}

			this.tableNum3 = new ArrayList<Integer>();
			for(int i=0;i < this.controlCount * 6 * this.reelLength * this.reelLength * 2;i++) {
				this.tableNum3.add(stream.read());
			}
			System.out.printf("制御数:%d リール長:%d 小役数:%d ライン数:%d 滑りテーブル長:%d%n",controlCount,reelLength,yakuCount,maxLine,slideTableSize);
		}catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				buffer.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void setReelControlCode(int code) {
		this.rcc = code;
	}
	public int reelStop(int reel,int pos) {
		int slide;
		int num = this.getTableNum(reel);
		slide = this.slideTable.get(this.reelLength * num + pos);
		this.stopPos[this.stopCount] = (pos - slide + this.reelLength) % this.reelLength;
		this.reelStopPos[reel] = this.stopPos[this.stopCount];
		int ret = this.stopPos[this.stopCount];
		ret = pos - ret;
		if(ret < 0) {
			ret = this.reelLength + ret;
		}
		//System.out.println("pos:"+ret);
		this.stopCount = this.stopCount + 1 == 3 ? 0 : this.stopCount + 1;
		return ret;
	}
	private int getTableNum(int prm1)	{
		int ret = 0;
		int idx = 0;
		switch(this.stopCount) {
		case 0:
			ret = this.wpeek(this.tableNum1,(this.rcc * 3 + prm1) * 2);
			this.stopPattern = prm1 * 3;
			break;
		case 1:
			this.stopPattern += prm1 - 1;
			if(this.stopPattern > 3) {
				this.stopPattern--;
			}
			idx = this.rcc * 6 * this.reelLength;
			idx += this.stopPattern * this.reelLength;
			idx += this.stopPos[0];
			ret = wpeek(this.tableNum2,idx*2);
			break;
		case 2:
			idx = this.rcc * 6 * this.reelLength * this.reelLength;
            idx += this.stopPattern * this.reelLength * this.reelLength;
            idx += this.stopPos[0] * this.reelLength;
            idx += this.stopPos[1];
            ret = wpeek(this.tableNum3, (idx) * 2);
			break;
		}
		return ret;
	}
	public ArrayList<HitEvent> getHit(int betCoin) {
		int reelPos[] = new int[3];
		ArrayList<HitEvent> ret = new ArrayList<HitEvent>();
		for(int i=0;i<3;i++) {
			reelPos[i] = this.reel.getReelChar(i);
		}
	    for (int i = 0; i < this.maxLine; i++) {
	        boolean matrix[][] = {
	        		{false,false,false},
	        		{false,false,false},
	        		{false,false,false}
	    	};
	        ArrayList<Integer> line = this.betLine.get(i);
	        int[] lineChar = new int[3];
	        if (line.get(3) > betCoin) {
	            continue;
	        }
	        for (int j = 0; j < 3; j++) {
	            matrix[line.get(j)][j] = true;
	            lineChar[j] = this.reel.getReelChip(j,this.reel.getReelCharByIndex(j, line.get(j)));
	        }
	        for(int k=1;k<this.yakuCount;k++) {
	        	Yaku yaku = yakuList.get(k);
	        	int num = yaku.num;
	        	int yakuMode;
	        	int yakuArray[] = new int[3];
	        	yakuArray[0] = num & 0xf;
	        	num = num >> 4;
        		yakuArray[1] = num & 0xf;
        		num = num >> 4;
    			yakuArray[2] = num & 0xf;
    			num = num >> 4;
    			yakuMode = num & 0xf;
    			num = num >> 4;
				//System.out.printf("%d %d %d %n",yakuArray[0],yakuArray[1],yakuArray[2]);
				boolean hitFlag = true;
				for(int f=0;f<3;f++) {
					if((lineChar[f] == yakuArray[f] || yakuArray[f] == 0xF) && (yakuMode & (1 << (3-betCoin))) != 0) {
						
					}else {
						hitFlag = false;
						break;
					}
				}
				if(!hitFlag) continue;
				ret.add(new HitEvent(matrix, this.yakuList.get(k)));
	        }
	    }
	    return ret;
	}
	private int wpeek(ArrayList<Integer> arr,int idx) {
		int ret = 0;
		for(int i = 0;i < 2; i++) {
			ret += arr.get(idx + i) << (i * 8);
		}
		return ret;
	}
}