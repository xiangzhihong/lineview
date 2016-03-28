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

import android.graphics.Path;
import android.graphics.Region;

public class LinePoint {
	private String x;
	private float y = 0;
	private Path path;
	private Region region;
	public float fLineX;
	public float fLineY;

   //x轴为日期，y为分值
	public LinePoint(String x, float y) {
		this.x = x;
		this.y = y;
	}

	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}

}
