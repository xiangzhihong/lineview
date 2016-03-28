/*
    The Android Not Open Source Project
    Copyright (c) 2014-8-21 wangzheng <iswangzheng@gmail.com>

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
    @author wangzheng  DateTime 2014-8-21
 */

package com.xzh.chatview.view.graph;

import java.util.ArrayList;

public class Line {
    private ArrayList<LinePoint> points = new ArrayList<LinePoint>();
    private String color;
    private boolean showPoints = true;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public ArrayList<LinePoint> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<LinePoint> points) {
        this.points = points;
    }

    public void addPoint(LinePoint point) {
        points.add(point);
    }

    public LinePoint getPoint(int index) {
        if (index > (points.size() - 1)) {
            return points.get(0);
        }
        return points.get(index);
    }

    public int getSize() {
        return points.size();
    }

    public boolean isShowingPoints() {
        return showPoints;
    }

    public void setShowingPoints(boolean showPoints) {
        this.showPoints = showPoints;
    }

}
