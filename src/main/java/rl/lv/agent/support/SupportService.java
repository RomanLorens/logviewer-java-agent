package rl.lv.agent.support;

import rl.lv.agent.support.dto.MemoryStats;
import rl.lv.agent.support.dto.VersionInfo;

public interface SupportService {

	MemoryStats memoryDiagnostics();

	VersionInfo version();

	void stopServer();

}
