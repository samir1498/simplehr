package com.samir.simplehr.payroll.application.port.out;

import com.samir.simplehr.payroll.domain.model.PayrollAdjustment;

public interface PayrollAdjustmentRepositoryPort {
	PayrollAdjustment save(PayrollAdjustment adjustment);
}
