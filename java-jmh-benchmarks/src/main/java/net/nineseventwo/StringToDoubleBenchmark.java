/*
 * Copyright (c) 2005, 2014, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package net.nineseventwo;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.concurrent.TimeUnit;

public class StringToDoubleBenchmark {

	@State(Scope.Benchmark)
	public static class BenchmarkState {
		volatile String number = "0.99999999";
	}

	@Benchmark
	public double parseDouble(BenchmarkState state) {
		return Double.parseDouble(state.number);
	}

	@Benchmark
	public double doubleWrapper(BenchmarkState state) {
		return new Double(state.number).doubleValue();
	}

	@Benchmark
	public double bigDecimal(BenchmarkState state) {
		return new BigDecimal(state.number).doubleValue();
	}

	@Benchmark
	public double decimalFormat(BenchmarkState state) throws ParseException {
		DecimalFormat decimalFormat= new DecimalFormat();
		DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
		decimalFormatSymbols.setDecimalSeparator('.');
		decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);

		return decimalFormat.parse(state.number).doubleValue();
	}

	public static void main(String[] args) throws RunnerException {
		final Options options = new OptionsBuilder()
				.include(StringToDoubleBenchmark.class.getSimpleName())
				.warmupIterations(5)
				.measurementIterations(10)
				.forks(1)
				.mode(Mode.AverageTime)
				.timeUnit(TimeUnit.NANOSECONDS)
				.build();

		new Runner(options).run();
	}
}
