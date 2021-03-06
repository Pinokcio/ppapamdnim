import javax.swing.*;
import java.util.Collections;
import java.util.ArrayList;

public class Draw extends JComponent {
	static int mindPaneWidth = (int)(Frame.MindMapPane.getSize().getWidth()/2);
	static int mindPaneHeight = (int)(Frame.MindMapPane.getSize().getHeight()/2);
	static public void drawLabel() {
		Frame.MindMapPane.removeAll();
		for(int i = 0; i < MNode.nodeArray.size(); i++) {
			MNode.nodeArray.get(i).setLabel();
			if(!MNode.nodeArray.get(i).isSetted()) {
				setPosition(i); // x, y 좌표가 설정되어있지 않을 경우 필드에 그리기 전에 좌표 설정해줌. 
			}
			drawLabelBounds(MNode.nodeArray.get(i));
		}
		for(int i = 0; i < MNode.nodeArray.size(); i++) {
			Frame.MindMapPane.add(MNode.nodeArray.get(i).getLabel());
		}
		Frame.drawMind();
	}
	static public void setPosition(int i) {
		//Frame.drawField.set
		//root제외한 나머지 노드의 위치 조정
		MNode rootNode = MNode.nodeArray.get(0);
		int zeroX = rootNode.getX();
		int zeroY = rootNode.getY();
		
		ArrayList<Integer> cntChild = new ArrayList<Integer>();// 최하위 노드부터 최상위 노드들까지의 childnode개수를 저장.
		ArrayList<Integer> childNum = new ArrayList<Integer>();//최하위노드가 몇번째 child인지부터 최상위노드가 몇번째 child인지까지 저장
		ArrayList<Double> angle = new ArrayList<Double>();
		int r;
		double theta = 0;
		MNode tmpNode;
		MNode nodeForChildNum;
		int k=0;
		int cnt;
		tmpNode = MNode.nodeArray.get(i);
		
		while(tmpNode.getParent()!=null) {
			cnt = 0;
			cntChild.add(tmpNode.getCntChildren());
			childNum.add(k,cnt);
			nodeForChildNum = tmpNode.getParent().getFirstChild();
			if(nodeForChildNum==tmpNode)
				childNum.set(k, cnt);
			else {
				cnt++;
				while(nodeForChildNum.getNextSibling()!=tmpNode) {
					nodeForChildNum = nodeForChildNum.getNextSibling();
					cnt++;
				}
				childNum.set(k,cnt);
			}
			k++;
			tmpNode = tmpNode.getParent();
			}
		childNum.add(0);
		cntChild.add(tmpNode.getCntChildren());
		Collections.reverse(cntChild);
		Collections.reverse(childNum);
			
		if(cntChild.get(0)==1) {
			angle.add(360.0/2);
		}
		else
			angle.add(360.0/cntChild.get(0));
		theta = angle.get(0)*childNum.get(1);
			
		for(int j = 1; j < cntChild.size()-1; j++) {
			angle.add(angle.get(j-1) / (cntChild.get(j)+1));
			theta = theta - angle.get(j-1)/2 + angle.get(j)*(childNum.get(j+1)+1);
		}
		r = 100*MNode.nodeArray.get(i).getRank();
		MNode.nodeArray.get(i).setX(zeroX+(int)(r*Math.cos(Math.toRadians(theta))));
		MNode.nodeArray.get(i).setY(zeroY+(int)(r*Math.sin(Math.toRadians(theta))));
		
		childNum.clear();
		cntChild.clear();
		angle.clear();
	}
	static public void drawLabelBounds(MNode node) {
		node.getLabel().setBounds(node.getX(), node.getY(), node.getWidth(), node.getHeight());
	}
}