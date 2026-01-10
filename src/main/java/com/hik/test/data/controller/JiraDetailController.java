package com.hik.test.data.controller;

import com.hik.test.data.dto.JiraDetailDO;
import com.hik.test.data.service.JiraDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/jira")
@RequiredArgsConstructor
public class JiraDetailController {
    private final JiraDetailService jiraDetailService;

    @PostMapping
    public ResponseEntity<Long> createJira(@RequestBody JiraDetailDO jiraDetail) {
        Long id = jiraDetailService.createJira(jiraDetail);
        return ResponseEntity.ok(id);
    }

    @PostMapping("/batch")
    public ResponseEntity<Boolean> batchCreateJira(@RequestBody List<JiraDetailDO> jiraDetails) {
        boolean result = jiraDetailService.batchCreateJira(jiraDetails);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JiraDetailDO> getJiraById(@PathVariable Long id) {
        JiraDetailDO jiraDetail = jiraDetailService.getJiraById(id);
        return ResponseEntity.ok(jiraDetail);
    }

    @GetMapping("/number/{jiraNumber}")
    public ResponseEntity<JiraDetailDO> getJiraByNumber(@PathVariable String jiraNumber) {
        JiraDetailDO jiraDetail = jiraDetailService.getJiraByNumber(jiraNumber);
        return ResponseEntity.ok(jiraDetail);
    }


    @PutMapping
    public ResponseEntity<Boolean> updateJira(@RequestBody JiraDetailDO jiraDetail) {
        boolean result = jiraDetailService.updateJira(jiraDetail);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteJira(@PathVariable Long id) {
        boolean result = jiraDetailService.deleteJira(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/statistics/owner/{owner}")
    public ResponseEntity<List<Map<String, Object>>> getOwnerStatistics(@PathVariable String owner) {
        List<Map<String, Object>> statistics = jiraDetailService.getOwnerStatistics(owner);
        return ResponseEntity.ok(statistics);
    }

    @PostMapping("/sync")
    public ResponseEntity<Boolean> syncJiraData(@RequestBody List<JiraDetailDO> jiraDetails) {
        boolean result = jiraDetailService.syncJiraData(jiraDetails);
        return ResponseEntity.ok(result);
    }
}
