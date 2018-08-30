package net.engining.pg.batch.sdk.infrastructure;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;
import org.springframework.batch.item.file.LineCallbackHandler;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.querydsl.jpa.impl.JPAQueryFactory;

import net.engining.pg.batch.entity.model.BtSysChecklist;
import net.engining.pg.batch.entity.model.QBtSysChecklist;
import net.engining.pg.batch.sdk.file.FlatFileHeader;

/**
 * 
 * File Header 处理组件 批量任务，每读一行头数据都会调用该类handleLine方法；
 * 
 * 记录数据到BT_SYS_CHECKLIST表
 * 
 * @author luxue
 *
 */
public class FileHeaderLineCallbackHandler implements LineCallbackHandler {

	@PersistenceContext
	private EntityManager em;

	/**
	 * Header数据包含的行数
	 */
	private int headerLineNumber;

	/**
	 * 支持多个文件情况下，headerLineNumber的乘数
	 */
	private int mult = 1;

	/**
	 * 已读行数
	 */
	private int readLines = 0;

	private String delimiter = ",";

	private String batchSeq;

	private String inspectionCd;

	private Date bizDate;

	private FlatFileHeader fileHeader;

	private FlatFileHeader.Type headerType;

	@Override
	@Transactional
	public void handleLine(String line) {
		readLines++;

		// 记录文件头数据到BT_SYS_CHECKLIST表
		if (readLines <= headerLineNumber * mult) {

			QBtSysChecklist qCactSysChecklist = QBtSysChecklist.btSysChecklist;
			BtSysChecklist cactSysChecklist = new JPAQueryFactory(em).select(qCactSysChecklist).from(qCactSysChecklist)
					.where(qCactSysChecklist.batchSeq.eq(batchSeq), 
							qCactSysChecklist.inspectionCd.eq(inspectionCd),
							qCactSysChecklist.bizDate.eq(bizDate))
					.fetchOne();

			// 保存文件Header数据
			if (headerType.equals(FlatFileHeader.Type.SimpleInteger)) {
				fileHeader = new FlatFileHeader();
				fileHeader.setTotalLines(Integer.parseInt(line));

			}
			else if (headerType.equals(FlatFileHeader.Type.SimpleString)) {
				fileHeader = new FlatFileHeader();
				String[] head = StringUtils.split(line, delimiter);
				fileHeader.setHeadContent(JSON.toJSONString(head));

			}
			else if (headerType.equals(FlatFileHeader.Type.JsonString)) {
				fileHeader = new FlatFileHeader();
				fileHeader.setHeadContent(line);
			}

			//将文件头数据存入，以备后用
			if (fileHeader != null) {
				List<FlatFileHeader> list = new ArrayList<FlatFileHeader>();
				
				if (cactSysChecklist.getCheckBizData() != null) {
					//有数据的情况，往后追加
					list = JSONObject.parseArray(cactSysChecklist.getCheckBizData(), FlatFileHeader.class);
					list.add(fileHeader);
					cactSysChecklist.setCheckBizData(JSON.toJSONString(list));
				}
				else {
					list.add(fileHeader);
					cactSysChecklist.setCheckBizData(JSON.toJSONString(list));
				}
			}

		}
	}

	public void setInspectionCd(String inspectionCd) {
		this.inspectionCd = inspectionCd;
	}

	public void setBizDate(Date bizDate) {
		this.bizDate = bizDate;
	}

	public void setHeaderLineNumber(int headerLineNumber) {
		this.headerLineNumber = headerLineNumber;
	}

	public void setHeaderType(FlatFileHeader.Type headerType) {
		this.headerType = headerType;
	}

	public void setBatchSeq(String batchSeq) {
		this.batchSeq = batchSeq;
	}

	public int getMult() {
		return mult;
	}

	public void setMult(int mult) {
		this.mult = mult;
	}

	public int getReadLines() {
		return readLines;
	}

	public void setReadLines(int readLines) {
		this.readLines = readLines;
	}

	/**
	 * @param delimiter
	 *            the delimiter to set
	 */
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

}